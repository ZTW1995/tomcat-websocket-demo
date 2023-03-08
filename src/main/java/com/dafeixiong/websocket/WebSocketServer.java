package com.dafeixiong.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

/**
 * 一个websocket 服务端示例
 */
public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

    public WebSocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public WebSocketServer(InetSocketAddress address) {
        super(address);
    }

    public WebSocketServer(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    //在执行打开握手并且给定的 websocket 准备好写入后调用。
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //向客户端发送消息
        broadcast("new connection: " + handshake
                .getResourceDescriptor()); //向所有的客户端发送消息
        System.out.println(
                conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    //在 websocket 连接关闭后调用
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        System.out.println(conn + " has left the room!");
    }

    //从远程主机接收的字符串消息的回调
    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast(message);
        System.out.println(conn + ": " + message);
    }

    //从远程主机接收的二进制消息回调
    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        System.out.println(conn + ": " + message);
    }

    //发生错误时调用。 如果错误导致 websocket 连接失败onClose(WebSocket, int, String, boolean)将被额外调用。 此方法将主要因为 IO 或协议错误而被调用
    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
        }
    }

    //服务器启动成功的时候执行该操作

    @Override
    public void onStart() {
        System.out.println("Server started!");
        //用于丢失连接的间隔检查的设置器 值小于或等于 0 会导致检查被停用 单位为s
        setConnectionLostTimeout(100);
    }

}