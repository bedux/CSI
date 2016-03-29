package logics.versionUtils;

import exception.CustomException;
import interfaces.VersionedSystem;
import logics.models.db.Repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bedux on 23/02/16.
 */
public class RepositoryManager {
    private static RepositoryManager instance;
    Queue<Runnable> toDo = new ConcurrentLinkedQueue<>();

    private RepositoryManager() {

    }

    public static RepositoryManager getInstance() {
        if (instance == null) {
            instance = new RepositoryManager();
        }
        return instance;
    }


    public void AddRepo(Repository repositoryInfo) {
        Runnable runn = () -> {
            try {
                VersionedSystem sys = repositoryInfo.CreateSystem();
                //sys.clone();

            } catch (Exception inv) {
                throw new CustomException(inv);
            }
        };
        Thread t = new Thread(runn);
        t.start();
        return;

    }


}
