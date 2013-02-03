package server;

import java.io.IOException;
import java.net.Socket;

import communication.RemoteHost;

public class RemotePlayer extends RemoteHost {
    protected int color;
    protected boolean ready;
    
    public RemotePlayer(Socket socket) throws IOException {
        super(socket);
        this.ready = false;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
    
    

}
