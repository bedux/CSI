package logics.versionUtils;

import exception.CustumException;
import interfaces.VersionedSystem;
import logics.models.db.Repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bedux on 23/02/16.
 */
public class RepositoryManager  {
    Queue<Runnable> toDo = new ConcurrentLinkedQueue<>();
    private static RepositoryManager instance;
    private RepositoryManager(){

    }

    public static  RepositoryManager  getInstance(){
        if(instance==null){
            instance = new RepositoryManager();
        }
        return  instance;
    }




   public void AddRepo(Repository repositoryInfo){
      Runnable runn = () -> {
          try {
              VersionedSystem sys = repositoryInfo.CreateSystem();
              //sys.clone();

          }catch (Exception inv ){
              throw new CustumException(inv);
          }
      };
      Thread t = new Thread(runn);
      t.start();
      return;

    }


}
