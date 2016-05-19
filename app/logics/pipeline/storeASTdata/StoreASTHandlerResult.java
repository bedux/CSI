package logics.pipeline.storeASTdata;

import interfaces.HandlerResult;
import logics.analyzer.Package;
import logics.models.newDatabase.RepositoryVersion;

/**
 * Created by bedux on 16/04/16.
 */
public class StoreASTHandlerResult implements HandlerResult {
    public final RepositoryVersion repositoryVersion;
    public final Package root;

    public StoreASTHandlerResult(RepositoryVersion repositoryVersion,Package root) {
        this.repositoryVersion = repositoryVersion;
        this.root = root;
    }
}
