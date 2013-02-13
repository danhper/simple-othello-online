package com.tuvistavie.othello.server;

import java.awt.Point;
import java.util.ArrayList;

import com.tuvistavie.othello.communication.ActionManager;
import com.tuvistavie.othello.communication.BasicMessage;
import com.tuvistavie.othello.communication.Message;
import com.tuvistavie.othello.communication.MessageSender;
import com.tuvistavie.othello.communication.MoveContent;
import com.tuvistavie.othello.communication.MulticastMessageWrapper;
import com.tuvistavie.othello.communication.RemoteHost;
import com.tuvistavie.othello.communication.StringContent;
import com.tuvistavie.othello.communication.UnicastMessageWrapper;
import com.tuvistavie.othello.game.BoardController;
import com.tuvistavie.othello.game.BoardModel;

public class OthelloResponseHandler implements ResponseHandler {

    protected BoardModel boardModel;
    protected BoardController boardController;
    protected ArrayList<RemoteHost> players;
    
    public OthelloResponseHandler() {
        this.boardModel = new BoardModel();
        this.boardController = new BoardController(this.boardModel, false);
    }

    public OthelloResponseHandler(ArrayList<RemoteHost> players) {
        this.boardModel = new BoardModel();
        this.boardController = new BoardController(this.boardModel, false);
        this.players = players;
    }

    @Override
    public void sendResponse(Message message, RemoteHost sender) {  
        int ret = message.getReturnCode();
        String action = ActionManager.getAction(ret);
        if(action.matches("play|connect|quite|ready|over")) {
            MulticastMessageWrapper multicastContainer = new MulticastMessageWrapper(message, players);
            MessageSender.sendMulticastMessage(multicastContainer);
        } else {
            UnicastMessageWrapper unicastContainer = new UnicastMessageWrapper(message, sender);
            MessageSender.sendUnicastMessage(unicastContainer);
        }
    }

    @Override
    public synchronized Message generateResponse(Message message, RemoteHost sender) {
        int ret = message.getReturnCode();
        String action = ActionManager.getAction(ret);
        if(!ActionManager.isValidActionNumber(ret)) {
            return new BasicMessage(ActionManager.getActionNumber("error"), new StringContent("Wrong action."));
        }
        if(action.equals("play")) {
            return this.getMoveMessage((MoveContent)message.getContent());
        } else if(action.equals("color")) {
            return this.getConectionMessage((StringContent)message.getContent(), sender);
        } else if(action.equals("ready")) {
            return this.getReadyMessage(sender);
        } else {
            return new BasicMessage(ret);
        }
    }
    
    protected Message getReadyMessage(RemoteHost sender) {
        RemotePlayer player = (RemotePlayer)sender;
        player.setReady(true);
        if(this.players.size() < 2) {
            return new BasicMessage(ActionManager.getActionNumber("waiting"));
        }
        for(RemoteHost host : this.players) {
            if(!host.equals(sender) && !((RemotePlayer)host).isReady()) {
                return new BasicMessage(ActionManager.getActionNumber("waiting"));
            }
        }
        return new BasicMessage(ActionManager.getActionNumber("ready"));
    }
    
    protected Message getConectionMessage(StringContent content, RemoteHost sender) {
        int color = Integer.parseInt(content.getContent());
        RemotePlayer player = (RemotePlayer)sender;
        
        for(RemoteHost host : this.players) {
            if(!host.equals(sender)) {
                color = this.boardModel.getOtherColor(((RemotePlayer)host).getColor());
            }
        }
        player.setColor(color);
        return new BasicMessage(ActionManager.getActionNumber("color"),
                new StringContent(String.valueOf(color)));
    }

    protected Message getMoveMessage(MoveContent content) {
        int color = content.getColor();
        Point point = content.getPoint();
        if(color != this.boardModel.getCurrentColor()) {
            return new BasicMessage(ActionManager.getActionNumber("error"), new StringContent("Other player turn."));
        }
        if(!this.boardController.checkMove(point, color)) {
            return new BasicMessage(ActionManager.getActionNumber("error"), new StringContent("Impossible move"));
        }
        this.boardController.putPiece(point, false);
        return new BasicMessage(0, content);
    }
    
    public void checkGameOver() {
        if(this.boardController.isGameOver()) {
            int winner;
            if(boardController.hasWinner()) {
                winner = boardController.getWinner();
            } else {
                winner = 2;
            }
            Message message = new BasicMessage(ActionManager.getActionNumber("over"),
                    new StringContent(String.valueOf(winner)));
            this.sendResponse(message, null);
            this.boardController.resetGame();
            for(RemoteHost host : this.players) {
                ((RemotePlayer)host).setReady(false);
            }
        }
    }
    
    public void setPlayers(ArrayList<RemoteHost> players) {
        this.players = players;
    }

}
