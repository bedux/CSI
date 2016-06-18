package controllers;

import logics.versionUtils.WebSocketProgress;
import play.mvc.Controller;
import play.mvc.WebSocket;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bedux on 05/05/16.
 */
public class WebSocketConnection extends Controller {

    private static ConcurrentHashMap<Long,WebSocket> availableWebSocket = new ConcurrentHashMap<>();


    public static void PutWebSocket(Long id,WebSocket w){
        availableWebSocket.putIfAbsent(id,w);
    }

    public static WebSocket<String> sockHandler(Long id) {
        System.out.println("Are there any socket??");

        if(availableWebSocket.containsKey(id)) {
            System.out.println("Available");
            return availableWebSocket.get(id);

        }

        return new WebSocketProgress(id);

    }
}
