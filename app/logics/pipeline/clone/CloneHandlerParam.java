package logics.pipeline.clone;

import interfaces.HandlerParam;
import logics.models.form.RepoForm;


public class CloneHandlerParam implements HandlerParam {
    public RepoForm repoForm;

    public CloneHandlerParam(RepoForm repoForm) {
        this.repoForm = repoForm;
    }
}
