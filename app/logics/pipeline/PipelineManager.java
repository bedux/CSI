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
//
//
        TreeGeneratorHandlerResult treeForA = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        IComputeAttributeContainer width = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer height = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
        IComputeAttributeContainer color = new CountingExternalPackage();
        MetricsCharacteristics ms = new MetricsCharacteristics(width,height,color,"Discussion and import");
        AnalyserHandlerParam result = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForA.root,ms);
        new AnalyserHandler().process(result);
//

        TreeGeneratorHandlerResult treeForC = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        IComputeAttributeContainer width1 = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer height1 = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
        IComputeAttributeContainer color1 = new CountingExternalPackagePercentage();
        MetricsCharacteristics ms1 = new MetricsCharacteristics(width1,height1,color1,"Discussion and import percentage");
        AnalyserHandlerParam result2 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForC.root,ms1);
        result2.percentage = true;
        new AnalyserHandler().process(result2);
//
//
        TreeGeneratorHandlerResult treeForB = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        IComputeAttributeContainer widthDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer heightDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
        IComputeAttributeContainer colorDoc = new ComputeProportionOfTwoQuery(new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone()),
                new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath.clone()));
        MetricsCharacteristics ms2 = new MetricsCharacteristics(widthDoc,heightDoc,colorDoc,"Doc and Methods Percentage");
        AnalyserHandlerParam result1 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForB.root,ms2);
        result1.percentage = true;
        new AnalyserHandler().process(result1);
//
//
        TreeGeneratorHandlerResult treeForD = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        IComputeAttributeContainer widthDocPer = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer heightDocPer = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
        IComputeAttributeContainer colorDocPer=  new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath.clone());
        MetricsCharacteristics ms21 = new MetricsCharacteristics(widthDocPer,heightDocPer,colorDocPer,"Doc and Methods ");
        AnalyserHandlerParam result4 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForD.root,ms21);
        new AnalyserHandler().process(result4);


        TreeGeneratorHandlerResult treeForE = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        IComputeAttributeContainer widthDocPerP = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer heightDocPerP = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
        IComputeAttributeContainer colorDocPerP=  new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath.clone());
        MetricsCharacteristics ms22 = new MetricsCharacteristics(widthDocPerP,heightDocPerP,colorDocPerP,"Doc and Methods Package ");
        AnalyserHandlerParam result5 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForE.root,ms22);
        result5.isOnlyPackage=true;
        result5.percentage=false;
        new AnalyserHandler().process(result5);



        TreeGeneratorHandlerResult treeForF = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
        IComputeAttributeContainer widthDocPerF = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
        IComputeAttributeContainer heightDocPerF = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
        IComputeAttributeContainer colorDocPerF=  new CountingExternalPackage();
        MetricsCharacteristics ms23 = new MetricsCharacteristics(widthDocPerF,heightDocPerF,colorDocPerF,"Discussion Methods Package ");
        AnalyserHandlerParam result6 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForF.root,ms23);
        result6.isOnlyPackage=true;
        result6.percentage=false;
        new AnalyserHandler().process(result6);










    }
}
