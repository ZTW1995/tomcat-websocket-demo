package com.dafeixiong.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>项目名称：tomcat-demo</p>
 * <p>类名：com.dafeixiong.tomcat.ws.WsChatEndPoint</p>
 * <p>创建时间: 2022-11-07 13:30 </p>
 * <p>修改时间: 2022-11-07 13:30 </p>
 *
 * @author zhang.taowei
 * @version 1.0.0
 */
@ServerEndpoint(value = "/bear")
public class WsChatEndPoint {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    private static ConcurrentHashMap<Session, WsChatEndPoint> ssMap= new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        ssMap.put(session, this);
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session){
        ssMap.remove(this.session);
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        WsChatEndPoint tmp = ssMap.get(session);
        try {
            tmp.sendMessage("我：" + message);
            for (WsChatEndPoint chatEndPoint : ssMap.values()) {
                if (!tmp.equals(chatEndPoint)) {
                    chatEndPoint.sendMessage("游客：" + message);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WsChatEndPoint.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WsChatEndPoint.onlineCount--;
    }
}
