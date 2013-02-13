package com.tuvistavie.othello.communication;


public class MessageSender {
    public static void sendUnicastMessage(UnicastMessageWrapper messageContainer) {
        String message = messageContainer.getMessage().format();
        messageContainer.getRecipient().getOutFlux().println(message);
    }
    
    public static void sendMulticastMessage(MulticastMessageWrapper messageContainer) {
        String message = messageContainer.getMessage().format();
        for(RemoteHost host : messageContainer.getRecipients()) {
            host.getOutFlux().println(message);
        }
    }
}
