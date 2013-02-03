package communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class DefaultConnectionInfos {
    
    public static final int PORT = 56715;
    public static final String HOST = initHost();
    
    private DefaultConnectionInfos() {
        
    }
    
    private static String initHost() {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch(UnknownHostException e) {
            host = "localhost";
        }
        return host;
    }
    
    
}
