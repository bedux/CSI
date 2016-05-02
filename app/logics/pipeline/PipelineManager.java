package logics.pipeline;

import logics.models.db.RepositoryVersion;
import logics.models.form.RepoForm;
import logics.models.modelQuery.*;
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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


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
        try {
            CompletableFuture.runAsync(()-> {
                        TreeGeneratorHandlerResult treeForA = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
                        MetricsCharacteristics ms = new MetricsCharacteristics(Query::CountMethodByPath, Query::CountFieldByPath, Query::DiscussedImportMethodCounter, "Discussion and import");
                        AnalyserHandlerParam result = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion, treeForA.root, ms);
                        new AnalyserHandler().process(result);
                    }).thenAcceptAsync((x) -> {

                TreeGeneratorHandlerResult treeForB = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
                MetricsCharacteristics ms1 = new MetricsCharacteristics(Query::CountMethodByPath, Query::CountFieldByPath, Query::DiscussedImportMethodCounterOverTotal, "Discussion and import percentage");
                AnalyserHandlerParam result1 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion, treeForB.root, ms1);
                result1.percentage = true;
                new AnalyserHandler().process(result1);
            }).thenAcceptAsync((x) -> {

                        TreeGeneratorHandlerResult treeForC = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
                        MetricsCharacteristics ms2 = new MetricsCharacteristics(Query::CountMethodByPath, Query::CountFieldByPath, Query::JavaDocForMethodCount, "JavaDoc");
                        AnalyserHandlerParam result2 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion, treeForC.root, ms2);
                        new AnalyserHandler().process(result2);
                    }).thenAcceptAsync((x) -> {

                        TreeGeneratorHandlerResult treeForD = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
                        MetricsCharacteristics ms3 = new MetricsCharacteristics(Query::CountMethodByPath, Query::CountFieldByPath, Query::JavaDocOverTotalMethods, "JavaDoc over Method");
                        AnalyserHandlerParam result3 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion, treeForD.root, ms3);
                        result3.percentage = true;
                        new AnalyserHandler().process(result3);
                    }).thenAcceptAsync((x) -> {

            TreeGeneratorHandlerResult treeForE = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
            MetricsCharacteristics ms4 = new MetricsCharacteristics(Query::CountMethodByPath,Query::CountFieldByPath,Query::JavaDocForMethodCount,"JavaDoc only Package");
            AnalyserHandlerParam result4 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForE.root,ms4);
            result4.percentage = false;
            result4.isOnlyPackage = true;
            new AnalyserHandler().process(result4);
            }).thenAcceptAsync((x) -> {

            TreeGeneratorHandlerResult treeForF = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
            MetricsCharacteristics ms5 = new MetricsCharacteristics(Query::CountMethodByPath,Query::CountFieldByPath,Query::JavaDocOverTotalMethods,"JavaDoc over Method Package");
            AnalyserHandlerParam result5 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForF.root,ms5);
            result5.percentage = true;
            result5.isOnlyPackage = true;
            new AnalyserHandler().process(result5);
            }).thenAcceptAsync((x) -> {


            TreeGeneratorHandlerResult treeForG = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
            MetricsCharacteristics ms6 = new MetricsCharacteristics(Query::CountMethodByPath,Query::CountFieldByPath,Query::DiscussedImportMethodCounter,"Discussion only Package");
            AnalyserHandlerParam result6 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForG.root,ms6);
            result6.percentage = false;
            result6.isOnlyPackage = true;
            new AnalyserHandler().process(result6);
            }).thenAcceptAsync((x) -> {

            TreeGeneratorHandlerResult treeForH = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
            MetricsCharacteristics ms7 = new MetricsCharacteristics(Query::CountMethodByPath,Query::CountFieldByPath,Query::DiscussedImportMethodCounterOverTotal,"Discussion over Method and Import  Package");
            AnalyserHandlerParam result7 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForH.root,ms7);
            result7.percentage = true;
            result7.isOnlyPackage = true;
            new AnalyserHandler().process(result7);
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//
//


//        TreeGeneratorHandlerResult treeForC = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
//        IComputeAttributeContainer width1 = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
//        IComputeAttributeContainer height1 = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
//        IComputeAttributeContainer color1 = new CountingExternalPackagePercentage();
//        MetricsCharacteristics ms1 = new MetricsCharacteristics(width1,height1,color1,"Discussion and import percentage");
//        AnalyserHandlerParam result2 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForC.root,ms1);
//        result2.percentage = true;
//        new AnalyserHandler().process(result2);
////
////
//        TreeGeneratorHandlerResult treeForB = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
//        IComputeAttributeContainer widthDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
//        IComputeAttributeContainer heightDoc = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
//        IComputeAttributeContainer colorDoc = new ComputeProportionOfTwoQuery(new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone()),
//                new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath.clone()));
//        MetricsCharacteristics ms2 = new MetricsCharacteristics(widthDoc,heightDoc,colorDoc,"Doc and Methods Percentage");
//        AnalyserHandlerParam result1 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForB.root,ms2);
//        result1.percentage = true;
//        new AnalyserHandler().process(result1);
////
////
//        TreeGeneratorHandlerResult treeForD = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
//        IComputeAttributeContainer widthDocPer = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
//        IComputeAttributeContainer heightDocPer = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
//        IComputeAttributeContainer colorDocPer=  new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath.clone());
//        MetricsCharacteristics ms21 = new MetricsCharacteristics(widthDocPer,heightDocPer,colorDocPer,"Doc and Methods ");
//        AnalyserHandlerParam result4 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForD.root,ms21);
//        new AnalyserHandler().process(result4);
//
//
//        TreeGeneratorHandlerResult treeForE = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
//        IComputeAttributeContainer widthDocPerP = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
//        IComputeAttributeContainer heightDocPerP = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
//        IComputeAttributeContainer colorDocPerP=  new ComputeWithSingleQuery(QueryList.getInstance().countAllJavaDocInClassInterfaceByFilePath.clone());
//        MetricsCharacteristics ms22 = new MetricsCharacteristics(widthDocPerP,heightDocPerP,colorDocPerP,"Doc and Methods Package ");
//        AnalyserHandlerParam result5 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForE.root,ms22);
//        result5.isOnlyPackage=true;
//        result5.percentage=false;
//        new AnalyserHandler().process(result5);
//
//
//
//        TreeGeneratorHandlerResult treeForF = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
//        IComputeAttributeContainer widthDocPerF = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
//        IComputeAttributeContainer heightDocPerF = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
//        IComputeAttributeContainer colorDocPerF=  new CountingExternalPackage();
//        MetricsCharacteristics ms23 = new MetricsCharacteristics(widthDocPerF,heightDocPerF,colorDocPerF,"Discussion Methods Package ");
//        AnalyserHandlerParam result6 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForF.root,ms23);
//        result6.isOnlyPackage=true;
//        result6.percentage=false;
//        new AnalyserHandler().process(result6);
//
//
//        TreeGeneratorHandlerResult treeForG = new TreeGenerator().process(new TreeGeneratorHandleParam(shr.repositoryVersion));
//        IComputeAttributeContainer widthDocPerG = new ComputeWithSingleQuery(QueryList.getInstance().countAllMethodByFilePath.clone());
//        IComputeAttributeContainer heightDocPerG = new ComputeWithSingleQuery(QueryList.getInstance().countAllFieldsByFilePath.clone());
//        IComputeAttributeContainer colorDocPerG=  new CountingExternalPackagePercentage();
//        MetricsCharacteristics ms24 = new MetricsCharacteristics(widthDocPerG,heightDocPerG,colorDocPerG,"Discussion Methods Package Percentage");
//        AnalyserHandlerParam result7 = new AnalyserHandlerParam(storeASTHandlerResult.repositoryVersion,treeForG.root,ms24);
//        result7.isOnlyPackage=true;
//        result7.percentage=true;
//        new AnalyserHandler().process(result7);








    }
}
