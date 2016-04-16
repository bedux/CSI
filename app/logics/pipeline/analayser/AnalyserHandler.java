package logics.pipeline.analayser;

import com.fasterxml.jackson.databind.JsonNode;
import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.Features;
import logics.analyzer.Package;
import logics.analyzer.analysis.*;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;
import logics.models.query.*;
import logics.models.tools.MaximumMinimumData;
import play.Logger;
import play.libs.Json;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandler implements Handler<AnalyserHandlerParam, AnalyserHandlerResult> {


    //TODO
    /*
        Split Store and Computation:
        Store:(Repository Version Dependent)
            1)generate the tree
            2)Use ASTraversAndStore
        Computation:
            1)regenerate the tree -> possibility to customise the query that get all the files
            2)call computeCity  and the for (modify queryList when the return list is 0's length)






     */


    @Override
    public AnalyserHandlerResult process(AnalyserHandlerParam param) {
//        List<logics.models.db.File> components;
//        try {
//            components = QueryList.getInstance().getFileByRepositoryVersion(param.repositoryVersion.id);
//        } catch (Exception e) {
//            throw new CustomException(e);
//        }
//
//
//        File fileRoot = new File(Definitions.repositoryPath + param.repositoryVersion.id);
//        Package root = new Package(new Features("root", param.repositoryVersion.id.toString(), fileRoot.toPath()));
//        for (logics.models.db.File component : components) {
//            File helper = new File(Definitions.repositoryPath + component.path);
//            String s = clearPath(helper.toPath().normalize().toString(), param.repositoryVersion);
//            String dir = s.substring(0, s.indexOf('/'));
//            String remainName = s.substring(s.indexOf('/') + 1);
//            root.add(dir, helper.toPath(), remainName);
//        }
//
//
//        root.applyFunction(new ASTraversAndStore()::analysis).join();


        return  new AnalyserHandlerResult(computeCity(param.root, param.metricsToCompute.getWidth(), param.metricsToCompute.getHeight(), param.metricsToCompute.getColor(), param.metricsToCompute.getMetricType().replaceAll(" ", "_") + param.repositoryVersion.id, param.metricsToCompute.repositoryRender(param.repositoryVersion)));



    }

    private String clearPath(String s, RepositoryVersion rpv) {
        return s.substring(s.indexOf(Definitions.repositoryPathABS + rpv.id) + (Definitions.repositoryPathABS).length());

    }

    private JsonNode computeCity(Package root,IComputeAttributeContainer width,IComputeAttributeContainer height,IComputeAttributeContainer color,String resultName,RepositoryRender repoRender){
        Logger.info("Load data");
        try {
            root.applyFunction(new LoadFromDatabase(width, height, color)::analysis).get();
        } catch (InterruptedException e) {
          throw new CustomException();
        } catch (ExecutionException e) {
            throw new CustomException();
        }


        Logger.info("Maximum minimum ");
        MaximumMinimumData mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);

        Logger.info("Adjusting Size");
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);

        Logger.info("Packing Size");
        root.applyFunction(new PackingAnalyzer()::analysis);

        Logger.info("Depth analysis");
        int max = root.applyFunction(new DepthAnalyser()::analysis);

        Logger.info("Maximum Minimum remoteness ");
        mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);

        Logger.info("Coloring");
        root.applyFunction(new ColoringAnalyser(max, mmd)::analysis);


        Logger.info("Save result");
        JsonNode json = Json.toJson(root.getRenderJSON());
        if (Files.exists(new File(Definitions.jsonPath + resultName + ".json").toPath())) {
            try {
                Files.delete(new File(Definitions.jsonPath + resultName + ".json").toPath());
            } catch (IOException e) {
                throw new CustomException(e);
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Definitions.jsonPath +resultName + ".json"), "utf-8"))) {
            writer.write(Json.stringify(json));
            repoRender.localPath = ("/assets/data/" + resultName+ ".json");
            new SaveClassAsTable().save(repoRender);
            return  json;

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
