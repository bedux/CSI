package logics.pipeline.clone;

import interfaces.Handler;
import interfaces.HandlerResult;
import interfaces.VersionedSystem;
import logics.models.db.Repository;
import logics.models.db.RepositoryVersion;

/**
 * Created by bedux on 07/03/16.
 */
public class CloneHandler implements Handler<CloneHandlerParam,CloneHandlerResult> {

    @Override
    public CloneHandlerResult process(CloneHandlerParam param) {

        Repository repositoryInfo = Repository.CreareRepo(param.repoForm);
        VersionedSystem sys = repositoryInfo.CreateSystem();
        RepositoryVersion repositoryVersion = RepositoryVersion.RepositoryVersion(repositoryInfo);
        sys.clone(repositoryVersion.id.toString());
        repositoryVersion.setHss(sys.getCurrentVersion());
        repositoryVersion.update(repositoryVersion.id);

        return new CloneHandlerResult(repositoryVersion);
    }
}


