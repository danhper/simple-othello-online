package com.tuvistavie.othello.communication;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import com.tuvistavie.othello.observer.Observable;
import com.tuvistavie.othello.observer.Observer;

public class FluxReader extends Observable implements Runnable {
    protected RemoteHost host;
    protected boolean running;
    protected MessageHandler messageHandler;
    

    public FluxReader(RemoteHost host, MessageHandler messageHandler) {
        this.running = true;
        this.host = host;
        this.messageHandler = messageHandler;
        this.observers = new ArrayList<Observer>();
    }

    @Override
    public void run() {
        while(running) {
            try {
                String str;
                while((str = this.host.getInFlux().readLine()) != null) {
                    Message treatedMessage = this.messageHandler.handleMessage(str);
                    MessageWrapper treatedDatas = new UnicastMessageWrapper(treatedMessage, this.host);
                    this.notifyObservers(treatedDatas);
                }
            } catch(SocketException e) {
                this.notifyObservers();
            } catch(IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopReading() {
        running = false;
    }
    
    @Override
    public void notifyObservers() {
        for(Observer o : this.observers) {
            o.update(this.host);
        }
    }
}