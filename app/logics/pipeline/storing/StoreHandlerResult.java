package logics.pipeline.storing;

import interfaces.HandlerResult;
import logics.models.db.RepositoryVersion;

/**
 * Created by bedux on 07/03/16.
 */
public class StoreHandlerResult implements HandlerResult {
    public RepositoryVersion repositoryVersion;

    public StoreHandlerResult(RepositoryVersion repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }
}
