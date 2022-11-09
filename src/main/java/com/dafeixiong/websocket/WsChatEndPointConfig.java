package com.dafeixiong.websocket;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>项目名称：tomcat-demo</p>
 * <p>类名：com.dafeixiong.tomcat.ws.ExamplesConfig</p>
 * <p>创建时间: 2022-11-09 0:03 </p>
 * <p>修改时间: 2022-11-09 0:03 </p>
 *
 * @author zhang.taowei
 * @version 1.0.0
 */
public class WsChatEndPointConfig implements ServerApplicationConfig {

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> scanned) {
        Set<ServerEndpointConfig> result = new HashSet<>();
        if (scanned.contains(WsChatEndPoint.class)) {
            result.add(ServerEndpointConfig.Builder.create(WsChatEndPoint.class, "/bear").build());
        }
        return result;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
        Set<Class<?>> results = new HashSet<>();
        for (Class<?> clazz : scanned) {
            if (clazz.getPackage().getName().contains("ws")) {
                results.add(clazz);
            }
        }
        return results;
    }
}