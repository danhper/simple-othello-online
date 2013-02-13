package com.tuvistavie.othello.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
    private int[][] board;
    private Image[] pieceImage;
    private Image[] movingPieceImage;
    private Image possibilityImage;
    private Image puttingPieceImage;
    private MyPoint movingPiecePoint;
    private String text;
    private Rectangle2D.Double[][] rectangles;
    private Point currentPoint;
    private BoardModel model;
    private BoardController controller;

    public BoardPanel(BoardModel model, BoardController controller) {
        this.model = model;
        this.board = model.getBoard();
        this.controller = controller;
        
        pieceImage = new Image[2];
        movingPieceImage = new Image[2];
        rectangles = new Rectangle2D.Double[BoardModel.SIZE][BoardModel.SIZE];
        currentPoint = new Point();
        
        resetGame();
        makeRectangles();
        initImages();
    }

    public void resetGame() {
        movingPiecePoint = new MyPoint(-1, -1);
        text = "";
    }

    private void initImages() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            pieceImage[BoardModel.WHITE] = ImageIO.read(loader
                    .getResourceAsStream("white.png"));
            pieceImage[BoardModel.BLACK] = ImageIO.read(loader
                    .getResourceAsStream("black.png"));
            movingPieceImage[BoardModel.WHITE] = ImageIO.read(loader
                    .getResourceAsStream("white_trans.png"));
            movingPieceImage[BoardModel.BLACK] = ImageIO.read(loader
                    .getResourceAsStream("black_trans.png"));
            possibilityImage = ImageIO.read(loader
                    .getResourceAsStream("possibility.png"));
            puttingPieceImage = ImageIO.read(loader
                    .getResourceAsStream("piece_put.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(73, 142, 97));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        makeLines(g2d);
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(3));
        g.drawRect(0, 1, getWidth() - 1, getHeight() - 2);
        drawPieces(g2d);
        drawMovingPiece(g2d);
        drawPossibilities(g2d);
        drawText(g2d);
    }

    private void makeLines(Graphics2D g2d) {
        g2d.setColor(Color.white);
        for(int i = 1; i < BoardModel.SIZE; i++) {
            g2d.draw(new Line2D.Double(0, i * getCellHeight(), getWidth(), i
                    * getCellHeight()));
            g2d.draw(new Line2D.Double(i * getCellWidth(), 0, i
                    * getCellWidth(), getHeight()));
        }
    }

    private void drawPieces(Graphics2D g2d) {
        int xLength = (int) getCellWidth(), yLength = (int) getCellHeight();
        for(int i = 0; i < board.length; i++)
            for(int j = 0; j < board.length; j++)
                if(board[i][j] == BoardModel.WHITE
                        || board[i][j] == BoardModel.BLACK)
                    g2d.drawImage(pieceImage[board[i][j]], i * xLength
                            + xLength / 5, j * yLength + yLength / 5, null);
    }

    public void makeRectangles() {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                rectangles[i][j] = new Rectangle2D.Double(i * getCellWidth()
                        + 5, j * getCellHeight() + 5, getCellWidth() - 5,
                        getCellHeight() - 5);
            }
        }
    }

    public MyPoint getCoordinatesPoint(Point p) {
        for(int i = 0; i < BoardModel.SIZE; i++)
            for(int j = 0; j < BoardModel.SIZE; j++)
                if(rectangles[i][j].contains(p))
                    return new MyPoint(i, j);
        return null;
    }

    private void drawMovingPiece(Graphics2D g2d) {
        if(!model.isGameOver() && model.isInGame() && model.isPlaying()) {
            g2d.drawImage(movingPieceImage[this.model.getCurrentColor()],
                    movingPiecePoint.x, movingPiecePoint.y, null);
        }
    }

    private void drawPossibilities(Graphics2D g2d) {
        int xLength = (int) getCellWidth(), yLength = (int) getCellHeight();
        for(MyPoint p : this.controller.getPossibilities()) {
            Image image = p.equals(this.currentPoint) ? puttingPieceImage
                    : possibilityImage;
            g2d.drawImage(image, p.x * xLength + xLength / 5, p.y * yLength
                    + yLength / 5, null);
        }
    }

    public void drawText(Graphics2D g2d) {
        if(model.isGameOver()) {
            g2d.setColor(Color.red);
            g2d.setFont(new Font("Arial", Font.PLAIN, 48));
            g2d.drawString(text, 150, 250);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    private double getCellWidth() {
        return (double) getWidth() / BoardModel.SIZE;
    }

    private double getCellHeight() {
        return (double) getHeight() / BoardModel.SIZE;
    }
    
    public Rectangle2D.Double[][] getRectangles() {
        return this.rectangles;
    }
    
    public Point getCurrentPoint() {
        return this.currentPoint;
    }
    
    public MyPoint getMovingPiecePoint() {
        return this.movingPiecePoint;
    }

}
