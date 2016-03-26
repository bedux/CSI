package logics.pipeline.clone;

import exception.CustomException;
import interfaces.Handler;
import interfaces.HandlerResult;
import interfaces.VersionedSystem;
import logics.DatabaseManager;
import logics.databaseUtilities.SaveClassAsTable;
import logics.models.db.Repository;
import logics.models.db.RepositoryVersion;

import java.sql.SQLException;

/**
 * Created by bedux on 07/03/16.
 */
public class CloneHandler implements Handler<CloneHandlerParam,CloneHandlerResult> {

    @Override
    public CloneHandlerResult process(CloneHandlerParam param) {

        Repository repository = new Repository();
        repository.pwd = param.repoForm.pwd;
        repository.usr = param.repoForm.user;
        repository.url = param.repoForm.uri;
        repository.subversionType = param.repoForm.type;
        try {
            int id = new SaveClassAsTable().save(repository);
            repository = DatabaseManager.getInstance().getById(id,Repository.class);
            System.out.println("Save Repository as" +repository.id+"   "+id);
        } catch (Exception e){
            e.printStackTrace();
            throw new CustomException(e);
        }

        RepositoryVersion repositoryVersion = new RepositoryVersion();
        repositoryVersion.repositoryId = repository.id;
        try {
            int id = new SaveClassAsTable().save(repositoryVersion);
            repositoryVersion = DatabaseManager.getInstance().getById(id,RepositoryVersion.class);
        } catch (Exception e){
            throw new CustomException(e);
        }

        VersionedSystem sys = repository.CreateSystem();
        try {
            sys.clone(Integer.toString(repositoryVersion.id));
        } catch (Exception e){
//            repositoryInfo.delete();
            //TODO
            throw new CustomException(e);
         }
//        repositoryVersion.setHss(sys.getCurrentVersion());
//        repositoryVersion.update(repositoryVersion.id);

        return new CloneHandlerResult(repositoryVersion);
    }
}


