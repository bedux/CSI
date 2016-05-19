package logics.pipeline.storeASTdata;

import com.avaje.ebean.Ebean;
import interfaces.Handler;
import logics.analyzer.analysis.ASTraversAndStore;
import play.Logger;

import java.util.concurrent.ExecutionException;


public class StoreAstData implements Handler<StoreASTHandleParam, StoreASTHandlerResult> {
    /***
     *
     * @param  param TreeGeneratorHandleParam contains the repository version
     * @return a new tree
     */
    @Override
    public StoreASTHandlerResult process(StoreASTHandleParam param) {
        Logger.info("AST of java file ");
        System.out.println(param.wsp);
        Ebean.beginTransaction();
        try {
            param.root.applyFunction(new ASTraversAndStore(param.wsp)::analysis).get();
            Ebean.commitTransaction();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally {
            Ebean.endTransaction();
        }




        Logger.info("END of java file ");

        return new StoreASTHandlerResult(param.repositoryVersion,param.root);
    }


}
