package logics.pipeline.analayser;

import com.fasterxml.jackson.databind.JsonNode;
import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.*;
import logics.analyzer.Package;
import logics.analyzer.analysis.*;
import logics.models.db.ComponentInfo;
import logics.models.db.RepositoryVersion;
import logics.models.tools.MaximumMinimumData;
import play.libs.Json;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by bedux on 08/03/16.
 */
public class AnaliserHandler implements Handler<AnalyserHandlerParam,AnalyserHandlerResult>{

    @Override
    public AnalyserHandlerResult process(AnalyserHandlerParam param) {
       List<ComponentInfo> components =  ComponentInfo.find.where("repository = " + param.repositoryVersion.id).findList();

        File fileRoot = new File(Definitions.repositoryPath + param.repositoryVersion.id);
        Package root = new Package(new DataFeatures("root", param.repositoryVersion.id.toString(), fileRoot.toPath()));
        for(ComponentInfo component: components){
            File helper = new File(Definitions.repositoryPath + component.fileName);
            String s = clearPath(helper.toPath().normalize().toString(),param.repositoryVersion);
            String dir = s.substring(0, s.indexOf('/'));
            String remainName = s.substring(s.indexOf('/') + 1);
            root.add(dir, helper.toPath(), remainName);
        }

        root.applyFunction(new WordCountAnalyser()::analysis);
        root.applyFunction(new MethodCountAnalyser()::analysis);
        MaximumMinimumData mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);
        root.applyFunction(new PackingAnalyzer()::analysis);


        int max = root.applyFunction(new DepthAnalyser()::analysis);
        mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);
        root.applyFunction(new ColoringAnalyser(max,mmd)::analysis);
        root.applyFunction(new SaveFeaturesInDbAnalyser(param.repositoryVersion)::analysis);

        JsonNode json = Json.toJson(root.getRenderJSON());

        if(Files.exists(new File(Definitions.jsonPath+param.repositoryVersion.id+".json").toPath())){
            try {
                Files.delete(new File(Definitions.jsonPath+param.repositoryVersion.id+".json").toPath());
            } catch (IOException e) {
                new CustomException(e);
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Definitions.jsonPath+param.repositoryVersion.id+".json"), "utf-8"))) {
            writer.write(Json.stringify(json));
            param.repositoryVersion.setJson("/assets/data/"+param.repositoryVersion.id+".json");
            param.repositoryVersion.update();

        }catch (Exception e){
            new CustomException(e);
        }

        return new AnalyserHandlerResult(json);
    }

    private String clearPath(String s,RepositoryVersion rpv) {
        return s.substring(s.indexOf(Definitions.repositoryPathABS + rpv.id) + (Definitions.repositoryPathABS).length());

    }
}
