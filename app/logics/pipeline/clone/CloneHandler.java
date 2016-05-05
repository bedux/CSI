package logics.pipeline.clone;

import exception.CustomException;
import interfaces.Handler;
import interfaces.VersionedSystem;
import logics.databaseCache.DatabaseModels;
import logics.databaseUtilities.Pair;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.Repository;
import logics.models.db.RepositoryVersion;
import logics.models.modelQuery.Query;
import logics.versionUtils.GitRepo;
import play.Logger;

import java.util.Optional;

/**
 * Created by bedux on 07/03/16.
 */
public class CloneHandler implements Handler<CloneHandlerParam, CloneHandlerResult> {

    @Override
    public CloneHandlerResult process(CloneHandlerParam param)  {

        VersionedSystem sys = new GitRepo(param.repoForm);
        RepositoryVersion newerRepoVersion = DatabaseModels.getInstance().getEntity(RepositoryVersion.class).get();
        param.repoForm.addlistOfRepositoryVersion(newerRepoVersion);
        try {
            sys.clone(Long.toString(newerRepoVersion.getId()));
        } catch (Exception e) {
            throw new CustomException(e);
        }




        return new CloneHandlerResult(param.repoForm.getListOfRepositoryVersion().get(0));
    }
}


