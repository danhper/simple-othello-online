package com.tuvistavie.othello.game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tuvistavie.othello.communication.HostInfo;

public class ConnectWindow extends JDialog {
	private HostInfo host;
	private JTextField hostEdit;
	private JTextField portEdit;
	private boolean validated;
	
	public ConnectWindow(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.host = new HostInfo();  
		this.initGUI();
	}
	
	public HostInfo showConectWindow() {
	    this.validated = false;
		this.setVisible(true);
		return this.validated ? this.host : null;
	}

	private void initGUI() {
		JPanel container = new JPanel(new BorderLayout());
		JPanel center = new JPanel(new GridBagLayout());
		JPanel south = new JPanel(new FlowLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel hostLabel = new JLabel("Host: ");
		JLabel portLabel = new JLabel("Port: ");
		hostEdit = new JTextField(this.host.getHost());
		portEdit = new JTextField(this.host.getPort() + "");
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int port = 0;
				try {
					port = Integer.parseInt(portEdit.getText());
					host = new HostInfo(hostEdit.getText(), port);
					validated = true;
					setVisible(false);
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Port must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
				} 
			}
		});
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		center.add(hostLabel, c);
		c.ipadx = 200;
		c.gridx = 1;
		center.add(hostEdit, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 0;
		c.gridx = 0;
		c.gridy = 1;
		center.add(portLabel, c);
		c.gridx = 1;
		center.add(portEdit, c);
		south.add(ok);
		south.add(cancel);
		container.add(center, BorderLayout.CENTER);
		container.add(south, BorderLayout.SOUTH);
		setContentPane(container);
	}
}
