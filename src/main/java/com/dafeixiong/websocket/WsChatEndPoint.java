package com.dafeixiong.websocket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>项目名称：tomcat-demo</p>
 * <p>类名：com.dafeixiong.tomcat.ws.WsChatEndPoint</p>
 * <p>创建时间: 2022-11-07 13:30 </p>
 * <p>修改时间: 2022-11-07 13:30 </p>
 *
 * @author zhang.taowei
 * @version 1.0.0
 */
@ServerEndpoint(value = "/bear/{userId}")
public class WsChatEndPoint {

    /**
     * 在线客户端session集合
     */
    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId")String userId){
        System.out.println("打开连接成功！用户 [ " + userId + " ]已接入。");
        sessionMap.put(userId, session);
        System.out.println("当前在线人数为：" + sessionMap.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam(value = "userId")String userId){
        System.out.println("关闭连接成功！用户 [ " + userId + " ]已离开。");
        sessionMap.remove(userId);
        System.out.println("当前在线人数为：" + sessionMap.size());
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam(value = "userId")String userId){
        System.out.println("用户 [" + userId + "] 的连接发生错误！");
        System.out.println("用户 [" + userId + "] 的异常信息：\n" + error.getMessage());
        System.out.println("当前在线人数为：" + sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam(value = "userId")String userId) {
        System.out.println("来自[" + userId + "]的消息:" + message);
        try {
            for (String tmpUserId : sessionMap.keySet()) {
                if (!tmpUserId.equals(userId)) {
                    Session tmp = sessionMap.get(tmpUserId);
                    tmp.getBasicRemote().sendText(userId + "：" + message);
                }
            }
        } catch (IOException e1) {
            System.out.println("消息接收异常！");
        }
    }

}
