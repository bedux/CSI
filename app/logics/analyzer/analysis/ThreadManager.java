package logics.analyzer.analysis;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bedux on 15/04/16.
 */
public class ThreadManager {

    private static ThreadManager threadManager = new ThreadManager();

    private final ExecutorService scheduler =
            Executors.newFixedThreadPool(8);
    private final ExecutorService schedulerSingle =
            Executors.newFixedThreadPool(8);


    /***
     * SINGLETON
     */
    private ThreadManager(){
    }

    /***
     *
     * @return the current instance of the ThreadManager
     */
    public static ThreadManager instance(){
        return threadManager;
    }

    /**
     *
     * @return get the current executor scheduler
     */
    public ExecutorService getExecutor(){
        return scheduler;
    }




}
