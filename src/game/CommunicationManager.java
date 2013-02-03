package game;


import java.awt.Point;

import communication.ActionManager;
import communication.BasicMessage;
import communication.Message;
import communication.MessageSender;
import communication.MoveContent;
import communication.RemoteHost;
import communication.StringContent;
import communication.UnicastMessageWrapper;

public class CommunicationManager {
    private BoardModel model;
    private BoardController controller;
    private RemoteHost server;
    
    public CommunicationManager(BoardController controller, BoardModel model) {
        this.controller = controller;
        this.model = model;
    }
    
    public void manageIncomingMessage(Message message) {
        if(this.server == null) {
            this.controller.raiseError("You are not connected");
        }
        String action = ActionManager.getAction(message.getReturnCode());
        if(action.equals("connect")) {
            this.manageConnection();
        } else if(action.equals("color")) {
            this.manageColor(message);
        } else if(action.equals("waiting")) {
            this.manageWaiting();
        } else if(action.equals("ready")) {
            this.manageReady();
        } else if(action.equals("error")) {
            this.manageErrors(message);
        } else if(action.equals("play")) {
            this.mangeMove(message);
        }
    }
    
    public void sendMessage(Message message) {
        MessageSender.sendUnicastMessage(new UnicastMessageWrapper(message, this.server));
    }
    
    private void mangeMove(Message message) {
        MoveContent content = (MoveContent)message.getContent();
        int color = content.getColor();
        if(color != model.getCurrentColor()) {
            controller.raiseMinorError("An error has occured.");
            return;
        }
        Point point = content.getPoint();
        controller.putPiece(point, true);
    }
    
    private void manageErrors(Message message) {
        this.controller.raiseMinorError(message.getContent().format());
    }
    
    private void manageColor(Message message) {
        int color = Integer.parseInt(message.getContent().format());
        this.model.setPlayerColor(color);
        this.model.setNextPlayerColor(color);
    }
    
    private void manageReady() {
        this.controller.resetGame();
        this.controller.displayInformations("Game starting.");
    }
    
    private void manageWaiting() {
        this.controller.displayInformations("Wating for other player.");
    }
    
    private void manageConnection() {
        if(!this.model.isOnlineMode()) {
            Message message = new BasicMessage(ActionManager.getActionNumber("color"), 
                    new StringContent(String.valueOf(this.model.getPlayerColor()))); 
            this.controller.displayInformations("Player connected");
            this.controller.initGame();
            this.sendMessage(message);
            this.model.setOnlineMode(true);
        }
    }
    
    public void sendMove(Point point) {
        Message message = new BasicMessage(ActionManager.getActionNumber("play"),
                new MoveContent(model.getPlayerColor(), point));
        this.sendMessage(message);
    }
    
    public void sendReady() {
        Message readyMessage = new BasicMessage(ActionManager.getActionNumber("ready"));
        this.sendMessage(readyMessage);
    }
    
    public void setServer(RemoteHost server) {
        this.server = server;
    }
}
