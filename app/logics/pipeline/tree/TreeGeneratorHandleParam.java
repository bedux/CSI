package logics.pipeline.tree;

import interfaces.HandlerParam;
import logics.models.newDatabase.RepositoryVersion;

/**
 * Created by bedux on 16/04/16.
 */
public class TreeGeneratorHandleParam implements HandlerParam {
    public final RepositoryVersion repositoryVersion;

    public TreeGeneratorHandleParam(RepositoryVersion repositoryVersion) {
        this.repositoryVersion = repositoryVersion;
    }
}
