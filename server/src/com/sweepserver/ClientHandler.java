package com.sweepserver;

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

class ClientHandler extends Thread {

	final DataInputStream in;
	final DataOutputStream out;
	final Socket socket;
	GameHandler gameHandler;
	private int color;

	// Constructor
	public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out, GameHandler gameHandler) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.gameHandler = gameHandler;
		color = gameHandler.getColor(socket.getInetAddress().toString());
	}

	@Override
	public void run() {

		String received;
		String toreturn;
		try {
			while (true) {
				out.flush();
				received = in.readUTF();
				System.out.println(received);
				if (received.equals("color")) {
					out.writeInt(color);
					continue;
				}
				if (received.equals("exit")) {
					System.out.println("Client " + this.socket + " exits");
					int color = gameHandler.getColor(socket.getInetAddress().toString());
					gameHandler.colors[color] = null;
					gameHandler.deleteColor(color);
					this.socket.close();
					break;
				}
				
				if (received.equals("colormap")) {
					out.writeUTF("c"+Arrays.deepToString(gameHandler.colormap));
					continue;
				}
				
				if (received.equals("map")) {
					out.writeUTF("m"+gameHandler.getVisible(gameHandler.getColor(socket.getInetAddress().toString())));
					continue;
				}
				
				
				if (received.equals("state")) {
					int freeTiles = 0;
				}

				//parse request
				String[] coordArray = received.split(",",2);
				int clickX = Integer.parseInt(coordArray[0]);
				int clickY = Integer.parseInt(coordArray[1]);
				out.writeUTF("l"+gameHandler.onClick(clickX, clickY, color));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// closing resources
			this.in.close();
			this.out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}