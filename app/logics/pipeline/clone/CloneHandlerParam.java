package logics.pipeline.clone;

import interfaces.HandlerParam;
import logics.models.newDatabase.Repository;


public class CloneHandlerParam implements HandlerParam {
    public Repository repoForm;

    public CloneHandlerParam(Repository repoForm) {
        this.repoForm = repoForm;
    }
}
