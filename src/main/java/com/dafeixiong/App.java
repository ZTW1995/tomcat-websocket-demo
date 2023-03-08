package com.dafeixiong;

import com.dafeixiong.servlet.TestServlet;
import com.dafeixiong.websocket.WebSocketServer;
import com.dafeixiong.websocket.WsChatEndPointConfig;
import com.dafeixiong.websocket.WsChatEndPoint;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.websocket.server.WsSci;
import org.apache.tomcat.websocket.server.WsServerContainer;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>项目名称：tomcat-websocket-demo</p>
 * <p>类名：com.dafeixiong.App</p>
 * <p>创建时间: 2022-11-09 9:25 </p>
 * <p>修改时间: 2022-11-09 9:25 </p>
 *
 * @author zhang.taowei
 * @version 1.0.0
 */
public class App {

    private static final String baseDir = "D:\\tmp-tomcat";

    private static final String contextPath = "";

    private static final int port = 8888;

    public static void main(String[] args) throws LifecycleException, DeploymentException, ServletException, IOException, InstantiationException, IllegalAccessException {
        // 构建tomcat对象
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDir);
        // 构建Connector对象,此对象负责与客户端的连接
        Connector con = new Connector("HTTP/1.1");
        // 设置服务端的监听端口
        con.setPort(port);
        con.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
        con.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        tomcat.getService().addConnector(con);
        // 获取tomcat上下文
        Context ctx = tomcat.addContext(contextPath, null);
        // true时：相关 classes 或 jar 修改时，会重新加载资源，不过资源消耗很大
        ctx.setReloadable(false);
        ctx.setParentClassLoader(App.class.getClassLoader());
        
        Set<Class<?>> wsClasses = new HashSet<>();
        wsClasses.add(WsChatEndPoint.class);
//        wsClasses.add(WsChatEndPointConfig.class);
        // 添加WsSci初始化器（开启websocket功能）
        ctx.addServletContainerInitializer(new WsSci(), wsClasses);
        // 添加servlet
        Tomcat.addServlet(ctx, "test", TestServlet.class.getName());
        ctx.addServletMappingDecoded("/test", "test");

//        WsServerContainer wsc = (WsServerContainer)ctx.getServletContext().getAttribute("javax.websocket.server.ServerContainer");

        WebSocketThread thread = new WebSocketThread();
        thread.start();

        tomcat.init();
        // 启动tomcat
        tomcat.start();
        // 阻塞当前线程
        tomcat.getServer().await();
    }
}
