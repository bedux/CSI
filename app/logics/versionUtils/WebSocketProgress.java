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
        WebSocketConnection.availableWebSocket.putIfAbsent(id, this);
    }

    @Override
    public void start(int i) {

        status -= i;
    }

    @Override
    public void beginTask(String s, int i) {
        sendMessage("Begin Task  "+s + "  "+i);
    }

    @Override
    public void update(int i) {
        status -= i;

    }

    @Override
    public void endTask() {
        sendMessage("End Task ");
        Logger.info("End Task ");
    }
    public void endTask(String name) {
        sendMessage("End "+name);
        Logger.info("End  "+name);
    }


    @Override
    public boolean isCancelled() {
        return false;
    }

    private void sendMessage(String message){
        if(out!=null){
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
