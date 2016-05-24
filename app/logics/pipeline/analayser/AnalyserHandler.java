package logics.pipeline.analayser;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.WebSocketConnection;
import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.Package;
import logics.analyzer.analysis.*;
import logics.models.newDatabase.RepositoryRender;
import logics.models.tools.MaximumMinimumData;
import logics.versionUtils.WebSocketProgress;
import play.Logger;
import play.Play;
import play.libs.Json;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandler implements Handler<AnalyserHandlerParam, AnalyserHandlerResult> {


    /**
     *
     * @param param @see AnalyserHandlerParam
     * @return @see AnalyserHandlerResult
     */

    @Override
    public AnalyserHandlerResult process(AnalyserHandlerParam param) {
        return  new AnalyserHandlerResult(computeCity(param));
    }


    /**
     *
     * Compute te four basic metrics over the project
     * @param param
     * @return
     */
    private JsonNode computeCity(AnalyserHandlerParam param){
        WebSocketProgress currentProgress =(WebSocketProgress) WebSocketConnection.availableWebSocket.get(param.repositoryVersion.repository.id);

        final Package root = param.root;
        final String resultName = param.metricsToCompute.getMetricType().replaceAll(" ", "_") + param.repositoryVersion.id;
        RepositoryRender repoRender = param.metricsToCompute.repositoryRender(param.repositoryVersion);





        try {

            currentProgress.sendMessage("Loading Data from Database",param.metricsToCompute.getMetricType());


            root.applyFunction(new LoadFromDatabase(param.metricsToCompute.getWidth(), param.metricsToCompute.getHeight(), param.metricsToCompute.getColor())::analysis).exceptionally(x->{
               x.printStackTrace();
                return null;
               // throw new CustomException(x.getCause().getMessage());
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();

           // throw new CustomException(e);
        } catch (ExecutionException e) {
            e.printStackTrace();

           // throw new CustomException(e);
        }


        MaximumMinimumData mmd;

        currentProgress.sendMessage("Adjusting size of the city",param.metricsToCompute.getMetricType());
        if(!param.percentage) {
            Logger.info("Maximum minimum");

             mmd = root.applyFunction(new MaximumDimensionAnalyser()::analysis);
        }else{
             mmd = root.applyFunction(new MaximumDimensionAnalyserPercentage()::analysis);
        }


        Logger.info("Adjusting Size",param.metricsToCompute.getMetricType());
        root.applyFunction(new AdjustSizeAnalyser(mmd)::analysis);



        currentProgress.sendMessage("Packing",param.metricsToCompute.getMetricType());

        Logger.info("Packing Size");
        root.applyFunction(new PackingAnalyzer()::analysis);

        currentProgress.sendMessage("Depth analysis",param.metricsToCompute.getMetricType());

        Logger.info("Depth analysis");
        int max = root.applyFunction(new DepthAnalyser()::analysis);


        if(param.isOnlyPackage) {
            root.applyFunction(new AnalysePackageColor()::analysis);
        }



        currentProgress.sendMessage("Compute  remoteness of the packages");
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

        }else if(param.isOnlyPackage && param.percentage){
            root.applyFunction(new ColoringAnalyserOnlyPackage(100, mmd)::analysis);

        }
        else {
            float maxx =   root.applyFunction(new PackageColorAnalyser()::analysis);
            System.out.println("maximum is >"+maxx);
            root.applyFunction(new ColoringAnalyserOnlyPackage(maxx, mmd)::analysis);

        }

        currentProgress.sendMessage("Saving result",param.metricsToCompute.getMetricType());

        Logger.info("Save result as "+ Play.application().path().getAbsolutePath()+"/"+ Definitions.jsonPathABS + resultName + ".json" );
        JsonNode json = Json.toJson(root.getRenderJSON());
        if (Files.exists(new File(Play.application().path().getAbsolutePath()+"/"+ Definitions.jsonPathABS + resultName + ".json").toPath())) {
            try {
                Files.delete(new File(Play.application().path().getAbsolutePath()+"/"+ Definitions.jsonPathABS + resultName + ".json").toPath());
            } catch (IOException e) {
                throw new CustomException(e);
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Play.application().path().getAbsolutePath()+"/"+ Definitions.jsonPathABS +resultName + ".json"))))) {
            writer.write(Json.stringify(json));
            repoRender.localpath = ("/asset/" + resultName+ ".json");
            repoRender.save();

            writer.close();
            return  json;

        } catch (Exception e) {
            throw new CustomException(e);
        }
    }
}
