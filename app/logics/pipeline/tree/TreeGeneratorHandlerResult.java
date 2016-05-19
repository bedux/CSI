package logics.pipeline.tree;

import interfaces.HandlerResult;
import logics.analyzer.Package;
import logics.models.newDatabase.RepositoryVersion;

/**
 * Created by bedux on 16/04/16.
 */
public class TreeGeneratorHandlerResult implements HandlerResult {
    public final Package root;
    public final RepositoryVersion repositoryVersion;

    public TreeGeneratorHandlerResult(Package root,RepositoryVersion repositoryVersion) {
        this.root = root;
        this.repositoryVersion = repositoryVersion;
    }
}
