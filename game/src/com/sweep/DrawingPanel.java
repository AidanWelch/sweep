package com.sweep;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DrawingPanel extends JPanel {

	private static Game game;
	private MouseEvent mouse;
	
	public DrawingPanel() {

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				game.new MouseHandler(me.getX(), me.getY(), game);
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
				repaint();
			}
		});

	}

	public void start() {
		JFrame window = new JFrame("Sweep!");
		window.setSize(800, 800);
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	    window.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent event) {
	        	game.exit();
	        	window.dispose();
	            System.exit(0);
	        }
	    });
		window.add(this);
		window.setVisible(true);
		game = new Game();
		game.start();
		class GameLoop extends Thread {
			public GameLoop(DrawingPanel panel) {
				while(true) {
					repaint();
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		new GameLoop(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(20.0f));
		for (int x = 50; x < 700; x += 20) {
			for (int y = 50; y < 700; y += 20) {
				g.drawRect(x, y, 20, 20);
			}
		}
		for (int x = 0; x < 650; x += 20) {
			for (int y = 0; y < 650; y += 20) {
				switch(game.colormap[x/20][y/20]) {
					case 1:
						g.setColor(Color.RED);
						break;
					case 2:
						g.setColor(Color.BLUE);
						break;
					case 3:
						g.setColor(Color.GREEN);
						break;
					case 4:
						g.setColor(Color.YELLOW);
						break;
					default:
						g.setColor(Color.LIGHT_GRAY);
				}
				g.fillRect(x+51, y+51, 19, 19);
				if(game.map[x/20][y/20] == 9) {
					g.drawRect(x+55, y+55, 10, 10);
				} else if(game.map[x/20][y/20] > 0) {
					g.setColor(Color.BLACK);
					g.drawString(Byte.toString(game.map[x/20][y/20]), (x+55), (y+70));
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		DrawingPanel panel = new DrawingPanel();
		panel.start();
	}
	

}