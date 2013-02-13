package com.tuvistavie.othello.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class BoardMouseListener implements MouseListener, MouseMotionListener {
    private BoardController controller;
    private BoardPanel panel;
    
    public BoardMouseListener(BoardController controller, BoardPanel panel) {
        this.controller = controller;
        this.panel = panel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
      if(this.check()) {
            return;
        }
      for(MyPoint p : controller.getPossibilities()) {
        if(panel.getRectangles()[p.x][p.y].contains(e.getPoint())) {
          controller.handleMove(p);
        }
      }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if(this.check()) {
            return;
        }
        panel.getMovingPiecePoint().setCoordinates(e.getPoint().x - 15,
                e.getPoint().y - 15);
        for(MyPoint p : controller.getPossibilities()) {
            if(panel.getRectangles()[p.x][p.y].contains(e.getPoint())) {
                panel.getCurrentPoint().setLocation(p);
                break;
            } else {
                panel.getCurrentPoint().setLocation(-1, -1);
            }
        }
        panel.repaint();
    }
    
    public boolean check() {
        return controller.isGameOver() || !controller.getModel().isPlaying() || !controller.getModel().isInGame(); 
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

    
  }