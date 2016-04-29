package logics.pipeline.clone;

import exception.CustomException;
import interfaces.Handler;
import interfaces.VersionedSystem;
import logics.databaseCache.DatabaseModels;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.Repository;
import logics.models.db.RepositoryVersion;
import logics.models.query.QueryList;
import play.Logger;

/**
 * Created by bedux on 07/03/16.
 */
public class CloneHandler implements Handler<CloneHandlerParam, CloneHandlerResult> {

    @Override
    public CloneHandlerResult process(CloneHandlerParam param) {

        Repository repository = new Repository();
        repository.setPwd(param.repoForm.pwd);
        repository.setUsr(param.repoForm.user);
        repository.setUrl(param.repoForm.uri);
        repository.setSubversionType(param.repoForm.type);
        try {
            long id = new SaveClassAsTable().save(repository);
            repository =QueryList.getInstance().getById(id, Repository.class).orElseThrow(()->new CustomException("No repository found!"));
            Logger.info("Save Repository as" + repository.getId() + "   " + id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e);
        }

        RepositoryVersion repositoryVersion = new RepositoryVersion();
        repositoryVersion.setRepositoryConcrete(DatabaseModels.getInstance().getEntity(Repository.class,repository.getId()).get());
        try {
            long id = new SaveClassAsTable().save(repositoryVersion);
            repositoryVersion = QueryList.getInstance().getById(id, RepositoryVersion.class).orElseThrow(()->new CustomException("No repository version found"));
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


