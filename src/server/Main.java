package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import communication.DefaultConnectionInfos;
import communication.OthelloMessageHandler;

public class Main {
    public static void main(String[] args) {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch(UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Server server = Server.getInstance(DefaultConnectionInfos.PORT, 2, ip, 
                new OthelloMessageHandler(), new OthelloResponseHandler());
        try {
            server.startServer();
            System.out.println("Server started at " + server.getIp() + " on port " + server.getPort() + "." +
            		" Press Ctrl+C to end.");
            
        } catch(IOException e) {
            System.err.println("Error when launching the server.");
            System.exit(1);
        }
        try {
            server.listen();
        } catch(IOException e) {
            System.err.println("Error when listening.");
            System.exit(1);
        }
    }
}
