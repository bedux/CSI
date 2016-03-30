package logics.versionUtils;


import org.eclipse.jgit.lib.ProgressMonitor;
import play.Logger;

/**
 * Created by bedux on 23/02/16.
 */
public class SimpleProgressMonitor implements ProgressMonitor {

    private int status = 0;

    @Override
    public void start(int i) {
        status -= i;
    }

    @Override
    public void beginTask(String s, int i) {
        Logger.info("Begin Task " + s);
        status = i;
    }

    @Override
    public void update(int i) {
        Logger.info("Update Task " + status);
        status -= i;

    }

    @Override
    public void endTask() {
        Logger.info("End Task ");
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
