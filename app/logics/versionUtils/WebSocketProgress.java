package logics.versionUtils;


import controllers.WebSocketConnection;
import org.eclipse.jgit.lib.ProgressMonitor;
import play.Logger;
import play.libs.F;
import play.mvc.WebSocket;


/**
 * Created by bedux on 23/02/16.
 */
public class WebSocketProgress extends WebSocket<String> implements ProgressMonitor  {

    private int status = 0;
    private Long id;
    private Out<String> out = null;
    public WebSocketProgress(Long id){
        this.id = id;
        WebSocketConnection.PutWebSocket(id, this);
    }

    @Override
    public synchronized void start(int i) {

        status -= i;
    }

    @Override
    public synchronized void beginTask(String s, int i) {
        sendMessage("Begin Task  "+s + "  ");
    }

    @Override
    public synchronized  void update(int i) {
        status -= i;

    }

    @Override
    public synchronized void endTask() {
        sendMessage("End Task ");
        Logger.info("End Task ");
    }
    public synchronized void endTask(String name) {
        sendMessage("End "+name);
        Logger.info("End  "+name);
    }




    @Override
    public boolean isCancelled() {
        return false;
    }

    public synchronized  void  sendMessage(String message){
        if(out!=null){
            out.write(message);
        }
    }
    public synchronized  void  sendMessage(String ... messages){
        if(out!=null){
            String message="";
            for(String m:messages){
                message+=m+" ";
            }
            out.write(message);
        }
    }


    @Override
    public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
        in.onMessage(new F.Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                System.out.println(s);
            }
        });

        this.out = out;
    }
}
