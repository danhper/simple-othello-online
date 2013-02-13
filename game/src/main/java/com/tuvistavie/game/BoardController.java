package com.tuvistavie.othello.game;

import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.TreeSet;

import com.tuvistavie.othello.communication.FluxReader;
import com.tuvistavie.othello.communication.HostInfo;
import com.tuvistavie.othello.communication.OthelloMessageHandler;
import com.tuvistavie.othello.communication.RemoteHost;
import com.tuvistavie.othello.communication.UnicastMessageWrapper;
import com.tuvistavie.othello.observer.Observer;


public class BoardController implements Observer {
    private BoardModel model;
    private BoardView view;
    private Point nextMove;
    private FluxReader fluxReader;
    private CommunicationManager communicationManager;
    private RemoteHost server;

    public BoardController(BoardModel model, boolean visual) {
        this.model = model;
        if(visual) {
            BoardView view = new BoardView(this, model);
            this.model.addObserver(view);
            this.view = view;
        }
        this.fluxReader = null;
        this.communicationManager = new CommunicationManager(this, this.model);
    }
    
    public void launch() {
        this.view.initUI();
    }

    public void play() {
        if(model.isGameOver() || !model.isInGame()) {
            return;
        }
        if(model.getCurrentColor() != model.getPlayerColor()) {
            if(model.isOnlineMode()) {
                model.setPlaying(false);
            } else {
                computerPlay();
            }
        } else {
            model.setPlaying(true);
        }
    }

    public void playerChange(boolean show) {
        int tmpColor = (model.getCurrentColor() == BoardModel.BLACK ? BoardModel.WHITE
                : BoardModel.BLACK);
        if(canMove(tmpColor)) {
            model.setCurrentColor(tmpColor, show);
        } else if(!canMove(model.getCurrentColor())) {
            model.setGameOver(true, show);
        }
    }
    
    public void handleMove(Point p) {
        if(!model.isOnlineMode()) {
            putPiece(p, true);
        } else {
            communicationManager.sendMove(p);
        }
    }

    public ArrayList<Move> putPiece(Point p, boolean show) {
        ArrayList<Move> move = null;
        if(checkMove(p, model.getCurrentColor())) {
            move = model.putPiece(p, model.getCurrentColor(), show);
            playerChange(show);
        }
        if(show) {
            play();
        }
        return move;
    }

    public void computerPlay() {
        getNextMove(model.getComputerLevel(), model.getOtherColor(model.getPlayerColor()));
        putPiece(nextMove, true);
    }

    public boolean checkOneMove(MyPoint p, int color, MyPoint direction) {
        if(model.isEmpty(p.x, p.y))
            return false;
        if(model.isOwnPiece(p.x, p.y, color))
            return true;
        return checkOneMove(new MyPoint(p.x + direction.x, p.y + direction.y),
                color, direction);
    }

    public boolean checkMove(Point p, int color) {
        for(MyPoint direction : BoardModel.directions) {
            MyPoint next = new MyPoint(p.x + direction.x, p.y + direction.y);
            if(model.isOpponentPiece(next.x, next.y, color)
                    && checkOneMove(next, color, direction))
                return true;
        }
        return false;
    }

    public boolean hasWinner() {
        int[] scores = model.getScores();
        return scores[BoardModel.WHITE] != scores[BoardModel.BLACK];
    }
    
    public int getWinner() {
        int[] scores = model.getScores();
        return (scores[BoardModel.WHITE] > scores[BoardModel.BLACK]) ? BoardModel.WHITE : BoardModel.BLACK;
    }
    
    public String getWinnerName(int id) {
        return id == BoardModel.WHITE ? "White" : "Black";
    }

    public String getWinnerName() {
        int[] scores = model.getScores();
        return (scores[BoardModel.WHITE] > scores[BoardModel.BLACK]) ? "White"
                : "Black";
    }

    public BoardModel getModel() {
        return model;
    }

    public TreeSet<MyPoint> getPossibilities() {
        TreeSet<MyPoint> possibilities = new TreeSet<MyPoint>();
        for(MyPoint p : model.getFreeCases())
            if(checkMove(p, model.getCurrentColor()))
                possibilities.add(p);
        return possibilities;
    }

    public boolean canMove(int color) {
        for(MyPoint p : model.getFreeCases())
            if(checkMove(p, color))
                return true;
        return false;
    }

    public int evalPosition(int color) {
        if(model.getFreeCases().size() <= 16)
            return model.getScores()[color];
        int possibilitiesNumber = 0;
        for(MyPoint p : model.getFreeCases())
            if(checkMove(p, color))
                possibilitiesNumber++;
        return possibilitiesNumber;
    }

    public boolean isGameOver() {
        return (!canMove(BoardModel.BLACK) && !canMove(BoardModel.WHITE))
                || model.isGameOver();
    }
    
    public void initGame() {
        model.initGame();
    }

    public void resetGame() {
        model.startNewGame();
        play();
    }
    
    public void setReady() {
        if(model.isOnlineMode()) {
           communicationManager.sendReady(); 
        } else {
           resetGame();
        }
    }

    public int getNextMove(int depth, int color) {
        boolean self = (color == model.getCurrentColor());
        int[] oldScores = new int[] { model.getScores()[BoardModel.WHITE],
                model.getScores()[BoardModel.BLACK] };
        int oldColor = model.getCurrentColor();
        if(depth <= 0 || isGameOver())
            return evalPosition(model.getCurrentColor());
        int bestScore = self ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Point bestMove = new Point();
        for(Point p : getPossibilities()) {
            ArrayList<Move> move = putPiece(p, false);
            int score = getNextMove(depth - 1, color);
            for(Point corner : BoardModel.getCorners()) {
                if(corner.equals(p)) {
                    score += 50;
                }
            }
            model.cancelMove(move, oldScores, oldColor);
            if((self && score > bestScore) || (!self && score < bestScore)) {
                bestScore = score;
                bestMove = new MyPoint(p.x, p.y);
            }
        }
        nextMove = bestMove;
        return bestScore;
    }

    public void connect(HostInfo hostInfos) {
        if(hostInfos == null) {
            return;
        }
        try {
            InetAddress ip = InetAddress.getByName(hostInfos.getHost());
            Socket socket = new Socket(ip, hostInfos.getPort());
            server =  new RemoteHost(socket);
            this.fluxReader = new FluxReader(server, 
                    new OthelloMessageHandler());
            this.communicationManager.setServer(server);
            fluxReader.addObserver(this);
            Thread t = new Thread(fluxReader);
            t.start();
            this.model.setInGame(false);
        } catch(UnknownHostException e) {
            this.view.showError("Bad IP entered.");
        } catch(IOException e) {
            this.view.showError("Unable to connect.");
        }
    }
    
    public void disconnect() {
        if(!model.isOnlineMode()) {
            return;
        }
        if(this.fluxReader == null) {
            return;
        }
        this.fluxReader.stopReading();
        this.fluxReader = null;
        this.communicationManager.setServer(null);
        this.model.setOnlineMode(false);
        try {
            this.server.getSocket().close();
        } catch(IOException e) {
            this.raiseError("An error has occured.");
        }
        this.view.manageConnection();
    }
    
    public void displayInformations(String message) {
        if(this.model.isOnlineMode()) {
            this.view.setStatusText(message);
        }
    }
    
    public void raiseMinorError(String error) {
        if(this.model.isOnlineMode()) {
            this.view.setStatusText("Error : " + error);
        }
    }
    
    public void raiseError(String error) {
        this.view.showError(error);
    }

    @Override
    public void update(Object arg) {
        if(arg instanceof RemoteHost) {
            this.disconnect();
        } else if(arg instanceof UnicastMessageWrapper) {
            this.communicationManager.manageIncomingMessage(((UnicastMessageWrapper) arg).getMessage());
        }
    }
}
