package logics.pipeline;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bedux on 16/04/16.
 */
public class StatusObject {

    private AtomicInteger countFile = new AtomicInteger();
    final int totalFile;

    public synchronized void incFileExplored(){
        countFile.incrementAndGet();
    }

    public synchronized int getStatus(){
        return countFile.get();
    }

    public StatusObject(int totalFile){
        this.totalFile = totalFile;
    }

}
