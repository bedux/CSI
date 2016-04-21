package logics.analyzer.analysis;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by bedux on 15/04/16.
 */
public class ThreadManager {

    private static ThreadManager threadManager = new ThreadManager();

    private final ExecutorService scheduler =
            Executors.newFixedThreadPool(8);
    private ThreadManager(){

    }

    public static ThreadManager instance(){
        return threadManager;
    }

    public Executor getExecutor(){
        return scheduler;
    }


}
