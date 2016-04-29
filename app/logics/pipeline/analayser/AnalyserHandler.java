package logics.pipeline.analayser;

import com.fasterxml.jackson.databind.JsonNode;
import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.Features;
import logics.analyzer.Package;
import logics.analyzer.analysis.*;
import logics.databaseCache.DatabaseModels;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;
import logics.models.query.*;
import logics.models.tools.MaximumMinimumData;
import org.h2.engine.Database;
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
        DatabaseModels.getInstance().invalidCache();
        return  new AnalyserHandlerResult(computeCity(param.root, param.metricsToCompute.getWidth(), param.metricsToCompute.getHeight(), param.metricsToCompute.getColor(), param.metricsToCompute.getMetricType().replaceAll(" ", "_") + param.repositoryVersion.getId(), param.metricsToCompute.repositoryRender(param.repositoryVersion),param));
    }





    private JsonNode computeCity(Package root,IComputeAttributeContainer width,IComputeAttributeContainer height,IComputeAttributeContainer color,String resultName,RepositoryRender repoRender,AnalyserHandlerParam param){

        Logger.info("Load data");

        try {
            root.applyFunction(new LoadFromDatabase(width, height, color)::analysis).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




        MaximumMinimumData mmd;



        if(!param.percentage) {
            Logger.info("Maximum minimum ");
             mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);
        }else{
             mmd = root.applyFunction(new MaximumDimensionAnalyserPercentage()::analysis);
        }


        Logger.info("Adjusting Size");
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);

        Logger.info("Packing Size");
        root.applyFunction(new PackingAnalyzer()::analysis);

        Logger.info("Depth analysis");
        int max = root.applyFunction(new DepthAnalyser()::analysis);


        if(param.isOnlyPackage) {
            root.applyFunction(new AnalysePackageColor()::analysis);
        }



        Logger.info("Maximum Minimum remoteness ");
        if(!param.percentage) {
            mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);
        }else{
            mmd = root.applyFunction(new MaximumDimensionAnalyserPercentage()::analysis);
        }



        if(!param.percentage && !param.isOnlyPackage) {

            root.applyFunction(new ColoringAnalyser(max, mmd)::analysis);

        }else if(!param.isOnlyPackage){

            root.applyFunction(new ColoringAnalyserPercentage(max, mmd)::analysis);

        }else{
            float maxx =   root.applyFunction(new PackageColorAnalyser()::analysis);
            System.out.println("maximum is >"+maxx);
            root.applyFunction(new ColoringAnalyserOnlyPackage(maxx, mmd)::analysis);

        }

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
            repoRender.setLocalPath( "/assets/data/" + resultName+ ".json");
            new SaveClassAsTable().save(repoRender);
            return  json;

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
