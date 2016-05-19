package logics.pipeline.clone;

import exception.CustomException;
import interfaces.Handler;
import interfaces.VersionedSystem;
import logics.models.newDatabase.RepositoryVersion;
import logics.versionUtils.GitRepo;

/**
 * Created by bedux on 07/03/16.
 */
public class CloneHandler implements Handler<CloneHandlerParam, CloneHandlerResult> {

    @Override
    public CloneHandlerResult process(CloneHandlerParam param)  {

        VersionedSystem sys = new GitRepo(param.repoForm);
        RepositoryVersion newerRepoVersion = new RepositoryVersion();
        param.repoForm.repositoryVersionList.add(newerRepoVersion);
        newerRepoVersion.repository = param.repoForm;
        newerRepoVersion.save();
        param.repoForm.update();

        try {
            sys.clone(Long.toString(newerRepoVersion.id));
        } catch (Exception e) {
            throw new CustomException(e);
        }

        return new CloneHandlerResult(param.repoForm.repositoryVersionList.get(0));
    }
}


