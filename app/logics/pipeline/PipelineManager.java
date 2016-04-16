package logics.pipeline;

import logics.models.db.RepositoryVersion;
import logics.models.form.RepoForm;
import logics.models.query.*;
import logics.pipeline.analayser.AnalyserHandler;
import logics.pipeline.analayser.AnalyserHandlerParam;
import logics.pipeline.analayser.MetricsCharacteristics;
import logics.pipeline.clone.CloneHandler;
import logics.pipeline.clone.CloneHandlerParam;
import logics.pipeline.clone.CloneHandlerResult;
import logics.pipeline.storeASTdata.StoreASTHandleParam;
import logics.pipeline.storeASTdata.StoreASTHandlerResult;
import logics.pipeline.storeASTdata.StoreAstData;
import logics.pipeline.storing.StoreHandler;
import logics.pipeline.storing.StoreHandlerParam;
import logics.pipeline.storing.StoreHandlerResult;
import logics.pipeline.tree.TreeGenerator;
import logics.pipeline.tree.TreeGeneratorHandleParam;
import logics.pipeline.tree.TreeGeneratorHandlerResult;


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
        TreeGeneratorHandlerResult generateTree = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        StoreASTHandlerResult  storeASTHandlerResult = new StoreAstData().process(new StoreASTHandleParam(generateTree));


        TreeGeneratorHandlerResult treeForA = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));

        IComputeAttributeContainer width = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer height = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());

        IComputeAttributeContainer color = new CountingExternalPackage();
        MetricsCharacteristics ms = new MetricsCharacteristics(width,height,color,"Discussion and import");
        AnalyserHandlerParam result = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForA.root,ms);
        new AnalyserHandler().process(result);


        TreeGeneratorHandlerResult treeForB = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        IComputeAttributeContainer widthDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer heightDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
        IComputeAttributeContainer colorDoc = new ComputeProportionOfTwoQuery(new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone()),
                new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath.clone()));
        MetricsCharacteristics ms2 = new MetricsCharacteristics(widthDoc,heightDoc,colorDoc,"Doc and Methods");
        AnalyserHandlerParam result1 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForB.root,ms2);
        new AnalyserHandler().process(result1);









    }
}
