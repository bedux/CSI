package logics.pipeline.storing;

import interfaces.HandlerParam;
import logics.models.db.RepositoryVersion;
import logics.pipeline.clone.CloneHandlerParam;
import logics.pipeline.clone.CloneHandlerResult;

/**
 * Created by bedux on 07/03/16.
 */
public class StoreHandlerParam implements HandlerParam {

    public StoreHandlerParam(RepositoryVersion repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }

    public StoreHandlerParam(CloneHandlerResult repositoryVersion) {
        this.repositoryVersion = repositoryVersion.repositoryVersion;
    }

    public RepositoryVersion repositoryVersion;




}
