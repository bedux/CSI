package logics.analyzer.analysis;

import java.util.concurrent.*;

/**
 * Created by bedux on 15/04/16.
 */
public class ThreadManager {

    private static ThreadManager threadManager = new ThreadManager();

    private final ExecutorService scheduler =
            Executors.newFixedThreadPool(1);
    private final ExecutorService schedulerSingle =
            Executors.newFixedThreadPool(1);

    public ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }

    private final ForkJoinPool forkJoinPool = new ForkJoinPool(2);

    private ThreadManager(){

    }

    public static ThreadManager instance(){
        return threadManager;
    }

    public ExecutorService getExecutor(){
        return scheduler;
    }

    public Executor getExecutorSingle(){
        return schedulerSingle;
    }



}
