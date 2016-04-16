package logics.pipeline.storeASTdata;

import interfaces.HandlerParam;
import logics.analyzer.*;
import logics.analyzer.Package;
import logics.models.db.RepositoryVersion;
import logics.pipeline.tree.TreeGeneratorHandlerResult;

/**
 * Created by bedux on 16/04/16.
 */
public class StoreASTHandleParam implements HandlerParam {

    public StoreASTHandleParam(RepositoryVersion repositoryVersion, Package root) {
        this.repositoryVersion = repositoryVersion;
        this.root = root;
    }
    public StoreASTHandleParam(TreeGeneratorHandlerResult treeGeneratorHandlerResult) {
        this.repositoryVersion = treeGeneratorHandlerResult.repositoryVersion;
        this.root = treeGeneratorHandlerResult.root;
    }
    public final RepositoryVersion repositoryVersion;
    public final Package root;
}
