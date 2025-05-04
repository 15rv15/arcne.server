package io.arcne;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameServer extends WebSocketServer {

    private final Set<WebSocket> clients = Collections.synchronizedSet(new HashSet<>());

    public GameServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("Client connected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Client disconnected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        for (WebSocket client : clients) {
            client.send(message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("WebSocket Error:");
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("GameServer started on port " + getPort());
    }

    public static void main(String[] args) {
        int port = 10000;
        GameServer server = new GameServer(port);
        server.start();
    }
}
