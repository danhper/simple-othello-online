package com.tuvistavie.othello.server;

import com.tuvistavie.othello.communication.Message;
import com.tuvistavie.othello.communication.RemoteHost;

public interface ResponseHandler {
    public void sendResponse(Message message, RemoteHost sender);
    public Message generateResponse(Message message, RemoteHost sender);
}
