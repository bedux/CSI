package logics.pipeline.clone;

import interfaces.HandlerResult;
import logics.models.db.RepositoryVersion;

/**
 * Created by bedux on 07/03/16.
 */
public class CloneHandlerResult implements HandlerResult {
    public final RepositoryVersion repositoryVersion;

    public CloneHandlerResult(RepositoryVersion repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }
}
