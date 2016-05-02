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
import play.Logger;

import java.util.Optional;

/**
 * Created by bedux on 07/03/16.
 */
public class CloneHandler implements Handler<CloneHandlerParam, CloneHandlerResult> {

    @Override
    public CloneHandlerResult process(CloneHandlerParam param)  {

        Repository repository = DatabaseModels.getInstance().getEntity(Repository.class).get();
        repository.setPwd(param.repoForm.pwd);
        repository.setUsr(param.repoForm.user);
        repository.setUrl(param.repoForm.uri);
        repository.setSubversionType(param.repoForm.type);
        try {
            long id = new SaveClassAsTable().save(repository);

            Optional<Repository> repoMayBe = Query.byId(new Pair(id, Repository.class));
            repository =repoMayBe.orElseThrow(() -> new CustomException("No repository found!"));
            Logger.info("Save Repository as" + repository.getId() + "   " + id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e);
        }

        RepositoryVersion repositoryVersion =  DatabaseModels.getInstance().getEntity(RepositoryVersion.class).get();
        repositoryVersion.setRepositoryConcrete(DatabaseModels.getInstance().getEntity(Repository.class,repository.getId()).get());
        try {
            long id = new SaveClassAsTable().save(repositoryVersion);
            Optional<RepositoryVersion> repoMayBe = Query.byId(new Pair(id, RepositoryVersion.class));

            repositoryVersion =repoMayBe.orElseThrow(() -> new CustomException("No repository version found"));
        } catch (Exception e) {
            throw new CustomException(e);
        }

        VersionedSystem sys = repository.CreateSystem();
        try {
            sys.clone(Long.toString(repositoryVersion.getId()));
        } catch (Exception e) {
//            repositoryInfo.delete();
            throw new CustomException(e);
        }
//        repositoryVersion.setHss(sys.getCurrentVersion());
//        repositoryVersion.update(repositoryVersion.id);

        return new CloneHandlerResult(repositoryVersion);
    }
}


