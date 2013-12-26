package com.gdiama.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/echo")
public class WebsocketEndpoint {

    private final List<Session> clients = new ArrayList<>();

    public WebsocketEndpoint() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run() {
                int i = new Random().nextInt(10000);
                for (Session client : clients) {
                    try {
                        client.getBasicRemote().sendText(i + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    @OnOpen
    public void onWebSocketConnect(Session sess) {
        clients.add(sess);
        System.out.println("Socket Connected: " + sess);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket Closed: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.out);
    }

    @OnMessage
    public String onMessage(String message) {
        return message;
    }

}
