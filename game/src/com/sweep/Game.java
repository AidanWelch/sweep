package com.sweep;

import java.awt.Graphics;

import java.awt.Color;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.net.*; 
import java.io.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Thread{
	
	Socket socket = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	
	public String ip = "yetanotherdomain.ddns.net";
	public int port = 25565;
	
	public byte color;
	
	public byte[][] map = new byte[33][33];
	public byte[][] colormap = new byte[33][33];
	
	public class MouseHandler extends Thread{
		public MouseHandler (int x, int y, Game game) {
			try {
				if(((x-50)/20) < 33 && ((y-50)/20) < 33 && ((y-50)/20) >= 0 && ((x-50)/20) >= 0) {
					out.writeUTF(((x-50)/20) + "," + ((y-50)/20));
					String recieved = in.readUTF();
					//if(recieved.charAt(0) == 'l') {
						recieved = recieved.substring(1);
						if(!recieved.equals("loss")) {
							String[] smapX = recieved.substring(2, recieved.length()-2).replaceAll(" ","").replaceAll("\\[","").replaceAll("\\]"," ").split(" ", -1);
							for(int a = 0; a < smapX.length; a++) {
								System.out.println(smapX[a]);
								if(a != 0) {
									smapX[a] = smapX[a].substring(1);
								}
								String[] smapY = smapX[a].split(",",-1);
								for(int b = 0; b < smapY.length; b++) {
									if(smapY[b] != "") {
										game.map[a][b] = Byte.parseByte(smapY[b]);
									}
								}
							}
						} else {
							Arrays.fill(map, 9);
							Arrays.fill(colormap,0);
						}
					}
				//}
			} catch(IOException e) {
				System.out.println(e);
			}
		}
	}
	

	
	public void start() {

		try {
			socket = new Socket(ip, port);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			//game loop
			try {
				while(true) {
					
					out.writeUTF("colormap");
					Thread.sleep(17);
					String recieved = in.readUTF();
					//if(recieved.charAt(0) == 'c') {
						recieved = recieved.substring(1);
						String[] scolormapX = recieved.substring(2, recieved.length()-2).replaceAll(" ","").replaceAll("\\[","").replaceAll("\\]"," ").split(" ", -1);
						for(int x = 0; x < scolormapX.length; x++) {
							System.out.println(scolormapX[x]);
							if(x != 0) {
								scolormapX[x] = scolormapX[x].substring(1);
							}
							String[] scolormapY = scolormapX[x].split(",",-1);
							for(int y = 0; y < scolormapY.length; y++) {
								if(scolormapY[y] != "") {
									colormap[x][y] = Byte.parseByte(scolormapY[y]);
								}
							}
						//}
					}
					out.writeUTF("map");
					Thread.sleep(17);
					recieved = in.readUTF();
					//if(recieved.charAt(0) == 'm') {
						recieved = recieved.substring(1);
						String[] smapX = recieved.substring(2, recieved.length()-2).replaceAll(" ","").replaceAll("\\[","").replaceAll("\\]"," ").split(" ", -1);
						for(int a = 0; a < smapX.length; a++) {
							System.out.println(smapX[a]);
							if(a != 0) {
								smapX[a] = smapX[a].substring(1);
							}
							String[] smapY = smapX[a].split(",",-1);
							for(int b = 0; b < smapY.length; b++) {
								if(smapY[b] != "") {
									map[a][b] = Byte.parseByte(smapY[b]);
								}
							}
						}	
					//}
					
				}
			} catch(IOException | InterruptedException e) {
				e.printStackTrace();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void exit() {
		try {
			out.writeUTF("exit");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
