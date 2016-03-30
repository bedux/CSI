package logics.pipeline.analayser;

import com.fasterxml.jackson.databind.JsonNode;
import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.Features;
import logics.analyzer.Package;
import logics.analyzer.analysis.*;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.RepositoryVersion;
import logics.models.query.ComputeWithSingleQuery;
import logics.models.query.IComputeAttributeContainer;
import logics.models.query.QueryList;
import logics.models.tools.MaximumMinimumData;
import play.libs.Json;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandler implements Handler<AnalyserHandlerParam, AnalyserHandlerResult> {

    @Override
    public AnalyserHandlerResult process(AnalyserHandlerParam param) {
        List<logics.models.db.File> components;
        try {
            components = QueryList.getInstance().getFileByRepositoryVersion(param.repositoryVersion.id);
        } catch (Exception e) {
            throw new CustomException(e);
        }


        File fileRoot = new File(Definitions.repositoryPath + param.repositoryVersion.id);
        Package root = new Package(new Features("root", param.repositoryVersion.id.toString(), fileRoot.toPath()));
        for (logics.models.db.File component : components) {
            File helper = new File(Definitions.repositoryPath + component.path);
            String s = clearPath(helper.toPath().normalize().toString(), param.repositoryVersion);
            String dir = s.substring(0, s.indexOf('/'));
            String remainName = s.substring(s.indexOf('/') + 1);
            root.add(dir, helper.toPath(), remainName);
        }


        //TODO
        root.applyFunction(new ASTraversAndStore()::analysis);

        IComputeAttributeContainer width = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath);
        IComputeAttributeContainer height = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath);
        IComputeAttributeContainer color = QueryList.getInstance().ratioJavaDocMethodsByPath;

//

        root.applyFunction(new LoadFromDatabase(width, height, color)::analysis);

//            root.applyFunction(new WordCountAnalyser()::analysis);
//            root.applyFunction(new MethodCountAnalyser()::analysis);


        MaximumMinimumData mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);
        root.applyFunction(new PackingAnalyzer()::analysis);
//
//
        int max = root.applyFunction(new DepthAnalyser()::analysis);
        mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);
        root.applyFunction(new ColoringAnalyser(max, mmd)::analysis);
//
        JsonNode json = Json.toJson(root.getRenderJSON());

        if (Files.exists(new File(Definitions.jsonPath + param.repositoryVersion.id + ".json").toPath())) {
            try {
                Files.delete(new File(Definitions.jsonPath + param.repositoryVersion.id + ".json").toPath());
            } catch (IOException e) {
                throw new CustomException(e);
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Definitions.jsonPath + param.repositoryVersion.id + ".json"), "utf-8"))) {
            writer.write(Json.stringify(json));
            param.repositoryVersion.localPath = ("/assets/data/" + param.repositoryVersion.id + ".json");
            new SaveClassAsTable().update(param.repositoryVersion);
            return new AnalyserHandlerResult(json);


        } catch (Exception e) {
            throw new CustomException(e);
        }

    }

    private String clearPath(String s, RepositoryVersion rpv) {
        return s.substring(s.indexOf(Definitions.repositoryPathABS + rpv.id) + (Definitions.repositoryPathABS).length());

    }
}
