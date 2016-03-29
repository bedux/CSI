package logics.pipeline.analayser;

import interfaces.HandlerParam;
import logics.models.db.RepositoryVersion;
import logics.pipeline.storing.StoreHandlerResult;

import javax.validation.constraints.NotNull;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandlerParam implements HandlerParam {
    public RepositoryVersion repositoryVersion;

    public AnalyserHandlerParam(@NotNull RepositoryVersion repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }

    public AnalyserHandlerParam(@NotNull StoreHandlerResult storeHandlerResult) {
        this.repositoryVersion = storeHandlerResult.repositoryVersion;
    }

}
