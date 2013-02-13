package com.tuvistavie.othello.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.tuvistavie.othello.communication.FluxReader;
import com.tuvistavie.othello.communication.MessageHandler;
import com.tuvistavie.othello.communication.RemoteHost;
import com.tuvistavie.othello.observer.Observer;

public abstract class AbstractServer implements Observer {

    protected ArrayList<RemoteHost> hosts;
    protected ArrayList<FluxReader> readers;
    protected int port;
    protected int maxClientsNumber;
    protected InetAddress ip;
    protected ServerSocket serverSocket;
    protected MessageHandler messageHandler;
    protected ResponseHandler responseHandler;

    /**
     * Class constructor
     * 
     * @param port Port number of the server
     * @param maxClientsNumber Maximum number of clients
     * @param ip Ip of the server
     * @param messageHandler Message handler to use
     * @param responseHandler Response handler to use
     */
    protected AbstractServer(int port, int maxClientsNumber, InetAddress ip,
            MessageHandler messageHandler, ResponseHandler responseHandler) {
        this.port = port;
        this.maxClientsNumber = maxClientsNumber;
        this.ip = ip;
        this.hosts = new ArrayList<RemoteHost>();
        this.readers = new ArrayList<FluxReader>();
        this.messageHandler = messageHandler;
        this.responseHandler = responseHandler;
    }

    /**
     * 
     * @throws IOException
     */
    public void startServer() throws IOException {
        this.serverSocket = new ServerSocket(this.port, this.maxClientsNumber,
                this.ip);
    }

    public void listen() throws IOException {
        try {
            while(true) {
                this.addClient(this.serverSocket.accept());
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            for(FluxReader reader : this.readers) {
                reader.stopReading();
            }
            this.serverSocket.close();
        }
    }
    
    public RemoteHost getHost(Socket socket) throws IOException {
        return new RemoteHost(socket);
    }

    public void addClient(Socket socket) throws IOException {
        socket.setSoTimeout(2 * 60 * 1000); // timeout 5 minutes
        RemoteHost host = this.getHost(socket);
        FluxReader fluxReader = new FluxReader(host, this.messageHandler);
        fluxReader.addObserver(this);
        Thread t = new Thread(fluxReader);
        this.hosts.add(host);
        this.readers.add(fluxReader);
        t.start();
    }

    public void removeClient(RemoteHost host) {
        int id = this.hosts.indexOf(host);
        this.readers.get(id).stopReading();
        this.readers.remove(id);
        this.hosts.remove(id);
    }
    
    public String getServersInfo() {
        return "Server started at " + this.ip + " on port " + this.port + ".";
    }

    public String getIp() {
        return this.ip.getHostAddress();
    }

    public int getPort() {
        return this.port;
    }
}
