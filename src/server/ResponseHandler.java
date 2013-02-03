package server;

import communication.Message;
import communication.RemoteHost;

public interface ResponseHandler {
    public void sendResponse(Message message, RemoteHost sender);
    public Message generateResponse(Message message, RemoteHost sender);
}
