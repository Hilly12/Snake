package com.snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Frame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		JFrame F = new JFrame("GAME");
		F.setSize(1440, 800);
		F.setBackground(Color.black);
		F.setResizable(false);
		Board app = new Board(F, 1440, 800);
		F.add(app, BorderLayout.CENTER);
		F.setVisible(true);
		WindowAdapter W = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		F.addWindowListener(W);
	}
}