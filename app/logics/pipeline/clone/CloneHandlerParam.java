package logics.pipeline.clone;

import interfaces.HandlerParam;
import logics.models.db.Repository;
import logics.models.form.RepoForm;


public class CloneHandlerParam implements HandlerParam {
    public Repository repoForm;

    public CloneHandlerParam(Repository repoForm) {
        this.repoForm = repoForm;
    }
}
