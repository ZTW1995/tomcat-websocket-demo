package com.dafeixiong;

import com.dafeixiong.websocket.WebSocketServer;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketServerFactory;
import org.java_websocket.drafts.Draft;
import org.java_websocket.server.CustomSSLWebSocketServerFactory;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * <p>项目名称：tomcat-websocket-demo</p>
 * <p>类名：com.dafeixiong.WebSocketThread</p>
 * <p>创建时间: 2023-03-08 15:33 </p>
 * <p>修改时间: 2023-03-08 15:33 </p>
 *
 * @author zhang.taowei
 * @version 1.0.0
 */
public class WebSocketThread extends Thread {

    @Override
    public void run() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream keyStoreData = new FileInputStream("" + File.separatorChar + "");
            keyStore.load(keyStoreData, "certificatePass".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance( KeyManagerFactory.getDefaultAlgorithm() );
            kmf.init(keyStore, "KEYPASSWORD".toCharArray());

            TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(kmf.getKeyManagers(), trustFactory.getTrustManagers(), new SecureRandom("sr".getBytes(StandardCharsets.UTF_8)));
            SSLContext.setDefault(sslContext);
            DefaultSSLWebSocketServerFactory factory = new DefaultSSLWebSocketServerFactory(sslContext);


            int port = 8887; //客户端采用ws://localhost:8887进行连接
            WebSocketServer s = new WebSocketServer(port);
            s.start();
            System.out.println("ChatServer started on port: " + s.getPort());
            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String in = sysin.readLine();
                s.broadcast(in);
                if (in.equals("exit")) {
                    s.stop(1000);
                    break;
                }
            }
        } catch (IOException | InterruptedException | NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
