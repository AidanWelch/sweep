package com.sweepserver;

import java.io.*;
import java.util.Random;
import java.net.*;

public class Server {
	
	public int port = 25565;
	
    public void start(GameHandler gameHandler) { 
    	//I officially hate Java Sockets
    	try {
	        ServerSocket serverSocket = new ServerSocket(port); 
	        System.out.println("Started on port " + port);
	        
	        while (true) { 
	            Socket socket = null; 
	              
	            try { 
	                socket = serverSocket.accept(); 
	                  
	                System.out.println("A new client is connected : " + socket); 
	                DataInputStream in = new DataInputStream(socket.getInputStream()); 
	                DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
	                  
	                System.out.println("Assigning new thread for this client"); 
	  
	                // create a new thread object 
	                Thread handler = new ClientHandler(socket, in, out, gameHandler); 
	  
	                // Invoking the start() method 
	                handler.start(); 
	                  
	            } 
	            catch (Exception e){ 
	                socket.close(); 
	                e.printStackTrace(); 
	            } 
	            
	        }
	        
    	} catch(IOException e) {
    		e.printStackTrace();
    		System.exit(0);
    	}
    }
	
	
	public static void main(String[] args) {
		GameHandler gameHandler = new GameHandler();
		for(int x = 0; x < 33; x++) {
			for(int y = 0; y<33; y++) {
				System.out.print(gameHandler.map[x][y]);
				System.out.print(" ");
			}
			System.out.println();
		}
		
		Server server = new Server();
		server.start(gameHandler);
	}
}
