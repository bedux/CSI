package logics.pipeline.clone;

import exception.CustomException;
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
        try {
            sys.clone(repositoryVersion.id.toString());
        } catch (Exception e){
            repositoryInfo.delete();
            throw new CustomException();
         }
        repositoryVersion.setHss(sys.getCurrentVersion());
        repositoryVersion.update(repositoryVersion.id);

        return new CloneHandlerResult(repositoryVersion);
    }
}


