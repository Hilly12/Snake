package com.snake;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;

public class Board extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	private final int difficulty = 2;
	private int interval;
	private int width;
	private int height;
	private int squareSize;
	private int boardX;
	private int boardY;
	private int[][] board;
	private int[] tailX;
	private int[] tailY;
	private int tailSize;
	private boolean gameover;
	private direction dir;
	private Timer GameTimer;
	private int hPos, vPos;
	private int fruitX, fruitY;
	private int score;
	private boolean moving;
	
	enum direction {
		LEFT, RIGHT, UP, DOWN;
	}
	
	public Board(JFrame F, int appW, int appH) {
		super();
		F.setFocusable(true);
		F.addKeyListener(this);
		this.setSize(appW, appH);
		Setup();
	}
	
	public void Setup() {
		interval = (4 - difficulty) * 50;
		width = 36;
		height = 18;
		squareSize = 40;
		boardX = 0;
		boardY = 10;
		hPos = width / 2 + (int) (Math.random() * 4) - 2;
		vPos = height / 2 + (int) (Math.random() * 4) - 2;
		board = new int[width][height];
		int maxTailSize = (width - 2) * (height - 2);
		tailX = new int[maxTailSize];
		tailY = new int[maxTailSize];
		tailSize = 1;
		dir = direction.RIGHT;
		score = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
					board[x][y] = 1;
				} else {
					board[x][y] = 0;
				}
			}
		}
		fruitX = (int) (Math.random() * (width - 2)) + 1;
		fruitY = (int) (Math.random() * (height - 2)) + 1;
		board[fruitX][fruitY] = 2;
		moving = false;
		startGameTimer();
	}
	
	public void startGameTimer() {
		GameTimer = new Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int Xi = tailX[0];
				int Yi = tailY[0];
				int Xf, Yf;
				tailX[0] = hPos;
				tailY[0] = vPos;
				for (int i = 1; i < tailSize; i++) {
					Xf = tailX[i];
					Yf = tailY[i];
					tailX[i] = Xi;
					tailY[i] = Yi;
					Xi = Xf;
					Yi = Yf;
				}
				switch (dir) {
					case LEFT:
						hPos--;
						break;
					case RIGHT:
						hPos++;
						break;
					case UP:
						vPos--;
						break;
					case DOWN:
						vPos++;
						break;
					default:
						break;
				}
				moving = false;
				if (hPos <= 0 || width - 1 <= hPos || vPos <= 0 || height - 1 <= vPos) {
					gameover = true;
				}
				for (int i = 0; i < tailSize; i++) {
					if (hPos == tailX[i] && vPos == tailY[i]) {
						gameover = true;
					}
				}
				if (hPos == fruitX && vPos == fruitY) {
					score += 10 * difficulty;
					tailSize++;
					board[fruitX][fruitY] = 0;
					placeFruit();
				}
				if (gameover) {
					GameOver();
				}
				repaint();
			}
		});
		GameTimer.start();
	}
	
	public void placeFruit() {
		fruitX = (int) (Math.random() * (width - 2)) + 1;
		fruitY = (int) (Math.random() * (height - 2)) + 1;
		boolean validCoord = false;
		while (!validCoord) {
			fruitX = (int) (Math.random() * (width - 2)) + 1;
			fruitY = (int) (Math.random() * (height - 2)) + 1;
			validCoord = true;
			for (int i = 0; i < tailSize; i++) {
				if (fruitX == tailX[i] && fruitY == tailY[i]) {
					validCoord = false;
				}
			}
			if (hPos == fruitX && vPos == fruitY) {
				validCoord = false;
			}
		}
		board[fruitX][fruitY] = 2;
	}
	
	public void GameOver() {
		GameTimer.stop();
	}
	
	public void paint(Graphics g) {
		// The commented code here draws the snake as circle instead of square
		// blocks.
		// The head of the snake is a slightly larger circle with an animated
		// smile :)
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		Color snake = new Color(117, 0, 55);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (board[x][y] == 1) {
					g2.setColor(Color.DARK_GRAY);
					g2.fillRect(boardX + x * squareSize, boardY + y * squareSize, squareSize, squareSize);
				} else if (board[x][y] == 2) {
					g2.setColor(Color.LIGHT_GRAY);
					g2.fillOval(boardX + x * squareSize, boardY + y * squareSize, squareSize, squareSize);
				} else {
					g2.setColor(snake);
					for (int i = 0; i < tailSize; i++) {
						if (tailX[i] == x && tailY[i] == y) {
							if (i == tailSize - 1) {
								g2.fillRect(boardX + x * squareSize, boardY + y * squareSize, squareSize, squareSize);
							} else {
								g2.fillRect(boardX + x * squareSize, boardY + y * squareSize, squareSize, squareSize);
							}
						}
					}
				}
			}
		}
		g2.setColor(snake);
		g2.fillRect(boardX + hPos * squareSize, boardY + vPos * squareSize, squareSize, squareSize);
		g2.setColor(Color.BLACK);
		if (gameover) {
			// g2.drawLine(boardX + hPos * squareSize + squareSize / 4 + 2,
			// boardY + vPos * squareSize + 3 * squareSize / 4, boardX + hPos *
			// squareSize
			// + 3 * squareSize / 4 - 2, boardY + vPos * squareSize + 3 *
			// squareSize / 4);
			g2.setColor(Color.RED.darker());
			g2.setFont(new Font("Serif", 50, 50));
			g2.drawString("Game Over !", width * squareSize / 2 - 190, height * squareSize / 2 - 50);
			g2.drawString("Final Score: " + score, width * squareSize / 2 - 200, height * squareSize / 2);
			g2.drawString("Click space to play again", width * squareSize / 2 - 300, height * squareSize / 2 + 50);
		} else {
			// g2.fillOval(boardX + hPos * squareSize + 3, boardY + vPos *
			// squareSize + 1, squareSize - 6, squareSize - 10);
			// g2.setColor(snake);
			// g2.fillOval(boardX + hPos * squareSize, boardY + vPos *
			// squareSize, squareSize, squareSize - 10);
			g2.setColor(Color.WHITE);
			g2.drawString("Score: " + score, 1000, 30);
		}
		// g2.setColor(Color.BLACK);
		// g2.fillOval(boardX + hPos * squareSize + 3 * squareSize / 4 - 3,
		// boardY + vPos * squareSize + squareSize / 4 + 1, 6, 6);
		// g2.fillOval(boardX + hPos * squareSize + 3 * squareSize / 4 - 3,
		// boardY + vPos * squareSize + 3 * squareSize / 4 + 1, 6, 6);
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT && dir != direction.RIGHT && !moving) {
			dir = direction.LEFT;
			moving = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && dir != direction.LEFT && !moving) {
			dir = direction.RIGHT;
			moving = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP && dir != direction.DOWN && !moving) {
			dir = direction.UP;
			moving = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN && dir != direction.UP && !moving) {
			dir = direction.DOWN;
			moving = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE && gameover) {
			gameover = false;
			Setup();
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyTyped(KeyEvent e) {
	}
}