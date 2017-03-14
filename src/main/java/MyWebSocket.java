import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;

public class MyWebSocket extends WebSocketServer {
    public MyWebSocket(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public MyWebSocket(InetSocketAddress address) {
        super(address);
    }

    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        this.sendToAll("new connection: " + handshake.getResourceDescriptor());
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        this.sendToAll(conn + " has left the room!");
        System.out.println(conn + " has left the room!");
    }

    public void onMessage(WebSocket conn, String message) {
        this.sendToAll(message);
        System.out.println(conn + ": " + message);
    }

    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if(conn != null) {
            ;
        }
    }

    public void sendToAll(String text) {
        Collection con = this.connections();
        synchronized(con) {
            Iterator var4 = con.iterator();

            while(var4.hasNext()) {
                WebSocket c = (WebSocket)var4.next();
                c.send(text);
            }

        }
    }
}
