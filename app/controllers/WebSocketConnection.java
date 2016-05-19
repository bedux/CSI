package controllers;

import play.mvc.Controller;
import play.mvc.WebSocket;

import java.util.HashMap;

/**
 * Created by bedux on 05/05/16.
 */
public class WebSocketConnection extends Controller {

    public static HashMap<Long,WebSocket>  availableWebSocket = new HashMap<>();


    public static WebSocket<String> sockHandler(Long id) {
        System.out.println("Are there any socket??");

        if(availableWebSocket.containsKey(id)) {
            System.out.println("Available");
            return availableWebSocket.get(id);

        }

        return new WebSocket<String>() {
            @Override
            public void onReady(In<String> in, Out<String> out) {
                System.out.println("Fuck ypou!");
                out.write("Fuck youu");
            }
        };

    }
}
