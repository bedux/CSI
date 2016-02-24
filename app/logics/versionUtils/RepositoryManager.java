package logics.versionUtils;

import interfaces.VersionedSystem;
import logics.models.Repo;
import org.eclipse.jgit.revwalk.RevCommit;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
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




   public void AddRepo(Repo repoInfo){
      Runnable runn = () -> {
          try {
              VersionedSystem sys = repoInfo.CreateSystem();
              sys.clone();



          }catch (Exception inv ){

              Logger.error(inv.getMessage());
          }

      };
      Thread t = new Thread(runn);
      t.start();
      return;

    }


}
