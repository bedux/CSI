package logics.pipeline;

import logics.models.db.RepositoryRender;
import logics.models.db.RepositoryVersion;
import logics.models.form.RepoForm;
import logics.models.query.*;
import logics.pipeline.analayser.AnalyserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import logics.pipeline.analayser.MetricsCharatteristics;
import logics.pipeline.clone.CloneHandler;
import logics.pipeline.clone.CloneHandlerParam;
import logics.pipeline.clone.CloneHandlerResult;
import logics.pipeline.storing.StoreHandler;
import logics.pipeline.storing.StoreHandlerParam;
import logics.pipeline.storing.StoreHandlerResult;


public class PipelineManager {
    public void runPipeline(RepoForm repoForm) {
        CloneHandlerResult shr =  new CloneHandler().process(new CloneHandlerParam(repoForm));
        StoreAndAnalyze(shr);
    }

    public void StoreAndAnalyze(CloneHandlerResult repositoryVersion){
        StoreAndAnalyze(repositoryVersion.repositoryVersion);
    }

    public void StoreAndAnalyze(RepositoryVersion repositoryVersion){
        StoreHandlerResult shr =  new StoreHandler().process(new StoreHandlerParam(repositoryVersion));


        AnalyserHandlerParam result = new AnalyserHandlerParam(shr);
        IComputeAttributeContainer width = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath);
        IComputeAttributeContainer height = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath);
        IComputeAttributeContainer color = new CountingExternalPackage();

        IComputeAttributeContainer widthDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath);
        IComputeAttributeContainer heightDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath);
        IComputeAttributeContainer colorDoc = new ComputeProportionOfTwoQuery(new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath),
                new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath));


        MetricsCharatteristics ms = new MetricsCharatteristics(width,height,color,"Discussion and import");
        MetricsCharatteristics ms2 = new MetricsCharatteristics(widthDoc,heightDoc,colorDoc,"Doc and Methods");


        result.addMetrics(ms);
        result.addMetrics(ms2);



        new AnalyserHandler().process(result);




    }
}
