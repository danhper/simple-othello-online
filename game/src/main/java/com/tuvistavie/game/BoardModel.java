package com.tuvistavie.othello.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.TreeSet;

import com.tuvistavie.othello.observer.Observable;
import com.tuvistavie.othello.observer.Observer;

public class BoardModel extends Observable {
    public static final int SIZE = 8;
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int FREE = 2;
    public static final MyPoint[] directions = getDirections();
    public static final MyPoint[] corners = getCorners();

    private int[][] board;
    private TreeSet<MyPoint> freeCases;
    private int[] scores;
    private int currentColor;
    private boolean gameOver;
    private ArrayList<Move> lastMove;
    private int playerColor;
    private int computerLevel;
    private int nextPlayerColor;
    private int nextComputerLevel;
    private boolean playing;
    private boolean inGame;
    private boolean onlineMode;

    public BoardModel() {
        observers = new ArrayList<Observer>();
        board = new int[SIZE][SIZE];
        freeCases = new TreeSet<MyPoint>();
        lastMove = new ArrayList<Move>();
        gameOver = false;
        nextPlayerColor = BLACK;
        nextComputerLevel = 1;
        onlineMode = false;
        inGame = false;
        initGame();
    }

    public void initGame() {
        gameOver = false;
        scores = new int[] { 2, 2 };
        currentColor = BLACK;
        playerColor = nextPlayerColor;
        computerLevel = nextComputerLevel;
        initBoard();
        playing = (playerColor == currentColor);
        this.notifyObservers();
    }
    
    public void startNewGame() {
        inGame = true;
        initGame();
    }

    private static MyPoint[] getDirections() {
        MyPoint[] directions = new MyPoint[BoardModel.SIZE];
        int[] tmpX = new int[] { 0, 0, 1, 1, 1, -1, -1, -1 };
        int[] tmpY = new int[] { -1, 1, -1, 0, 1, -1, 0, 1 };
        for(int i = 0; i < BoardModel.SIZE; i++)
            directions[i] = new MyPoint(tmpX[i], tmpY[i]);
        return directions;
    }

    private void initBoard() {
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                board[i][j] = FREE;
        for(int i = 3; i < 5; i++)
            for(int j = 3; j < 5; j++)
                board[i][j] = (i + j) % 2;
        for(int i = 0; i < SIZE; i++)
            for(int j = 0; j < SIZE; j++)
                if(board[i][j] == FREE)
                    freeCases.add(new MyPoint(i, j));
    }

    public boolean reverseAll(Point p, int color, MyPoint direction) {
        if(isEmpty(p.x, p.y))
            return false;
        if(isOwnPiece(p.x, p.y, color))
            return true;

        boolean canReverse = reverseAll(new MyPoint(p.x + direction.x, p.y
                + direction.y), color, direction);
        if(canReverse)
            reverse(p, color);
        return canReverse;
    }

    public ArrayList<Move> putPiece(Point p, int color, boolean show) {
        lastMove = new ArrayList<Move>();
        reverse(p, color);
        for(MyPoint direction : directions) {
            MyPoint next = new MyPoint(p.x + direction.x, p.y + direction.y);
            if(isOpponentPiece(next.x, next.y, color))
                reverseAll(next, color, direction);
        }
        if(show) {
            notifyObservers();
        }
        return lastMove;
    }

    public void cancelMove(ArrayList<Move> toCancel, int[] oldScores,
            int oldColor) {
        for(Move move : toCancel) {
            MyPoint p = move.getPoint();
            board[p.x][p.y] = move.getBefore();
            if(move.getBefore() == FREE)
                freeCases.add(p);
        }
        currentColor = oldColor;
        gameOver = false;
        scores[BLACK] = oldScores[BLACK];
        scores[WHITE] = oldScores[WHITE];
    }

    private void reverse(Point p, int color) {
        lastMove.add(new Move(p, board[p.x][p.y], color));
        if(isOpponentPiece(p.x, p.y, color))
            scores[getOtherColor(color)]--;
        scores[color]++;
        board[p.x][p.y] = color;
        freeCases.remove(new MyPoint(p.x, p.y));
    }

    public boolean isOwnPiece(int x, int y, int color) {
        if(x < 0 || x >= SIZE || y < 0 || y >= SIZE || board[x][y] == FREE)
            return false;
        return board[x][y] == color;
    }

    public int getOtherColor(int color) {
        return (color == WHITE) ? BLACK : WHITE;
    }

    public boolean isOpponentPiece(int x, int y, int color) {
        if(x < 0 || x >= SIZE || y < 0 || y >= SIZE || board[x][y] == FREE)
            return false;
        return board[x][y] != color;
    }

    public boolean isEmpty(int x, int y) {
        if(x < 0 || x >= SIZE || y < 0 || y >= SIZE)
            return true;
        return board[x][y] == FREE;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[] getScores() {
        return scores;
    }

    public TreeSet<MyPoint> getFreeCases() {
        return freeCases;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor, boolean show) {
        this.currentColor = currentColor;
        if(show) {
            notifyObservers();
        }
    }

    public void setGameOver(boolean gameOver, boolean show) {
        this.gameOver = gameOver;
        this.inGame = false;
        this.playing = false;
        if(show) {
            notifyObservers();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public int getComputerLevel() {
        return computerLevel;
    }

    public void setComputerLevel(int computerLevel) {
        this.computerLevel = computerLevel;
    }

    public int getNextPlayerColor() {
        return nextPlayerColor;
    }

    public void setNextPlayerColor(int nextPlayerColor) {
        this.nextPlayerColor = nextPlayerColor;
    }

    public int getNextComputerLevel() {
        return nextComputerLevel;
    }

    public void setNextComputerLevel(int nextComputerLevel) {
        this.nextComputerLevel = nextComputerLevel;
    }
    
    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame= inGame;
    }
    
    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
        this.notifyObservers();
    }


    @Override
    public void notifyObservers() {
        for(Observer observer : observers)
            observer.update(board);
    }
    
    public static MyPoint[] getCorners() {
        return new MyPoint[] {
          new MyPoint(0, 0), 
          new MyPoint(0, SIZE - 1), 
          new MyPoint(SIZE - 1, 0), 
          new MyPoint(SIZE - 1, SIZE - 1)      
        };
    }

}
