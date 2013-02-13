package com.tuvistavie.othello.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;

import com.tuvistavie.othello.communication.HostInfo;
import com.tuvistavie.othello.observer.Observer;

public class BoardView extends JFrame implements Observer {
    private BoardPanel panel;
    private BoardController controller;
    private JPanel statusBar;
    private JLabel statusText;
    private BoardModel model;

    private JMenu levelMenu;
    private JMenuItem newGameItem;
    private JMenuItem connectionItem;
    private JMenuItem disconnectionItem;
    private JRadioButtonMenuItem[] colorItems;
    private final String defaultStatusText;
    

    public static final int boardSize = 500;

    public BoardView(BoardController controller, BoardModel model) {
        this.controller = controller;
        this.model = model;
        this.defaultStatusText = "Select level, color and start new game from menu.";
    }
    
    public void initUI() {
        this.setLayout(new BorderLayout());
        setTitle("Othello game");
        setSize(boardSize, boardSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        makeMenu();

        panel = new BoardPanel(this.model, this.controller);
        this.add(panel, BorderLayout.CENTER);
        this.initStatusBar();

        BoardMouseListener mouseListener = new BoardMouseListener(this.controller,
                this.panel);
        panel.addMouseListener(mouseListener);
        panel.addMouseMotionListener(mouseListener);

        setVisible(true);

        panel.makeRectangles();
    }
    
    public void showError(String text) {
        JOptionPane.showMessageDialog(this, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void initStatusBar() {
        this.statusBar = new JPanel();
        this.statusText = new JLabel();
        this.statusText.setHorizontalAlignment(SwingConstants.LEFT);
        this.statusText.setText(this.defaultStatusText);
        this.statusBar.add(this.statusText);
        this.statusBar.setPreferredSize(new Dimension(this.getWidth(), 20));
        this.add(this.statusBar, BorderLayout.SOUTH);
        this.statusBar.setVisible(true);
    }

    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Options");
        this.levelMenu = this.makeLevelMenu();
        options.add(this.makeColorMenu());
        options.add(this.levelMenu);
        menuBar.add(this.makeGameMenu());
        menuBar.add(options);
        setJMenuBar(menuBar);
    }

    private JMenu makeGameMenu() {
        JMenu game = new JMenu("Game");
        JMenuItem close = new JMenuItem("Close");

        this.newGameItem = new JMenuItem("New Game");

        this.newGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setReady();
                newGameItem.setEnabled(false);
            }
        });

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                controller.disconnect();
                System.exit(0);
            }
        });

        game.add(this.newGameItem);
        game.add(this.makeConnectionMenu());
        game.add(close);

        return game;
    }

    private JMenu makeColorMenu() {
        final int[] colors = new int[]{ BoardModel.WHITE, BoardModel.BLACK};
        JMenu colorMenu = new JMenu("Color");
        colorItems = new JRadioButtonMenuItem[] { 
            new JRadioButtonMenuItem("White"), new JRadioButtonMenuItem("Black")
        };
        ButtonGroup group = new ButtonGroup();
        colorItems[BoardModel.BLACK].setSelected(true);
        for( int i = 0; i < colors.length; i++) {
            colorMenu.add(colorItems[i]);
            group.add(colorItems[i]);
            final int color = colors[i];
            colorItems[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    model.setNextPlayerColor(color);
                }
            }); 
        }
        return colorMenu;
    }
   
    private JMenu makeLevelMenu() {
        JMenu levelMenu = new JMenu("Computer level");
        ButtonGroup group = new ButtonGroup();
        for(int i = 1; i <= 4; i++) {
            JRadioButtonMenuItem radioButton = new JRadioButtonMenuItem("Level " + i);
            if(i == this.model.getComputerLevel()) {
                radioButton.setSelected(true);
            }
            radioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String tmp = ((JRadioButtonMenuItem) e.getSource())
                            .getText();
                    model.setNextComputerLevel(Integer.parseInt((tmp.charAt(tmp
                            .length() - 1)) + ""));
                }
            });
            levelMenu.add(radioButton);
            group.add(radioButton);
        }
        return levelMenu;
    }

    private JMenu makeConnectionMenu() {
        JMenu connectionMenu = new JMenu("Online game");
        this.connectionItem = new JMenuItem("Connection");
        this.disconnectionItem = new JMenuItem("Disconnection");
        this.disconnectionItem.setEnabled(false);
        connectionMenu.add(this.connectionItem);
        connectionMenu.add(this.disconnectionItem);
        final JFrame parent = (JFrame) this;
        this.connectionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ConnectWindow connectWindow = new ConnectWindow(parent,
                        "Connection", true);
                HostInfo infos = connectWindow.showConectWindow();
                controller.connect(infos);
            }
        });
        
        this.disconnectionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                controller.disconnect();
            }
        });
        return connectionMenu;
    }
    
    public void setStatusText(String text) {
        this.statusText.setText(text);
        this.statusBar.setVisible(true);
        final Timer timer = new Timer();
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                statusBar.setVisible(false);
                timer.cancel();
            }
        }, 5 * 1000);
    }

    @Override
    public void update(Object arg) {
        if(!this.model.isInGame()) {
            this.statusText.setText(this.defaultStatusText);
            this.statusBar.setVisible(true);
        } else if(!this.model.isOnlineMode()) {
            this.statusBar.setVisible(false);
        }
        this.manageGameOver();
        if(model.isOnlineMode()) {
            this.manageConnection();
        }
        panel.repaint();
    }
    
    public void manageConnection() {
        boolean online = this.model.isOnlineMode();
        this.levelMenu.setEnabled(!online);
        this.newGameItem.setEnabled(!this.model.isInGame()|| this.model.isGameOver());
        this.connectionItem.setEnabled(!online);
        this.disconnectionItem.setEnabled(online);
    }
    
    private void manageGameOver() {
        if(model.isGameOver()) {
            if(controller.hasWinner())
                panel.setText(controller.getWinnerName() + " wins !");
            else
                panel.setText("Draw...");
        } else {
            panel.setText("");
        }
    }

}
