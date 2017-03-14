import org.java_websocket.WebSocketImpl;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class SSLWebSocketServer {
    public static void main(String[] args ) throws Exception {
        WebSocketImpl.DEBUG = true;

        MyWebSocket ws = new MyWebSocket( 8887 ); // Firefox does allow multible ssl connection only via port 443 //tested on FF16

        // load up the key store
        String STORETYPE = "JKS";
        String KEYSTORE = "key.store";
        String STOREPASSWORD = "123456";
        String KEYPASSWORD = "123456";

        KeyStore ks = KeyStore.getInstance( STORETYPE );
        File kf = new File( KEYSTORE );
        ks.load( new FileInputStream( kf ), STOREPASSWORD.toCharArray() );

        KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
        kmf.init( ks, KEYPASSWORD.toCharArray() );
        TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
        tmf.init( ks );

        SSLContext sslContext = null;
        sslContext = SSLContext.getInstance( "TLS" );
        sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );

        ws.setWebSocketFactory( new DefaultSSLWebSocketServerFactory( sslContext ) );

        ws.start();

        System.out.println( "SSL WS started on port: " + ws.getPort() );
    }
}
