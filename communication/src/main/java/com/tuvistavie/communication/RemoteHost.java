package com.tuvistavie.othello.communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class RemoteHost {
    protected Socket socket;
    protected BufferedReader inFlux;
    protected PrintWriter outFlux;
    
    public RemoteHost(Socket socket) throws IOException {
        this.socket = socket;
        BufferedReader inFlux = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        PrintWriter outFlux = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        this.inFlux = inFlux;
        this.outFlux = outFlux;
    }
    
    public RemoteHost(Socket socket, BufferedReader inFlux, PrintWriter outFlux) {
        this.socket = socket;
        this.inFlux = inFlux;
        this.outFlux = outFlux;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public BufferedReader getInFlux() {
        return this.inFlux;
    }
    
    public PrintWriter getOutFlux() {
        return this.outFlux;
    }
}
