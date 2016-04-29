package logics.pipeline.storeASTdata;

import exception.CustomException;
import interfaces.Handler;
import logics.Definitions;
import logics.analyzer.Features;
import logics.analyzer.Package;
import logics.analyzer.analysis.ASTraversAndStore;
import logics.analyzer.analysis.ThreadManager;
import logics.models.db.File;
import logics.models.db.RepositoryVersion;
import logics.models.query.QueryList;
import play.Logger;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


public class StoreAstData implements Handler<StoreASTHandleParam, StoreASTHandlerResult> {
    /***
     *
     * @param  param TreeGeneratorHandleParam contains the repository version
     * @return a new tree
     */
    @Override
    public StoreASTHandlerResult process(StoreASTHandleParam param) {
        Logger.info("AST of java file ");

        param.root.applyFunction(new ASTraversAndStore()::analysis);



        Logger.info("END of java file ");

        return new StoreASTHandlerResult(param.repositoryVersion,param.root);
    }


}
