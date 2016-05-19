package logics.pipeline.analayser;

import interfaces.HandlerParam;
import logics.analyzer.Package;
import logics.models.newDatabase.RepositoryVersion;
import logics.pipeline.storeASTdata.StoreASTHandlerResult;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandlerParam implements HandlerParam {
    public final RepositoryVersion repositoryVersion;
    public final Package root;
    public final MetricsCharacteristics metricsToCompute;
    public boolean percentage = false;
    public boolean isOnlyPackage = false;
//

//    public AnalyserHandlerParam(@NotNull RepositoryVersion repositoryVersion) {
//        this.repositoryVersion = repositoryVersion;
//    }
//
//    public AnalyserHandlerParam(@NotNull StoreHandlerResult storeHandlerResult) {
//        this.repositoryVersion = storeHandlerResult.repositoryVersion;
//    }


    public AnalyserHandlerParam(RepositoryVersion repositoryVersion, Package root,MetricsCharacteristics ms) {
        this.repositoryVersion = repositoryVersion;
        this.root = root;
        this.metricsToCompute = ms;
    }
    public AnalyserHandlerParam(StoreASTHandlerResult dta,MetricsCharacteristics ms) {
        this.repositoryVersion = dta.repositoryVersion;
        this.root = dta.root;
        this.metricsToCompute = ms;

    }





}
