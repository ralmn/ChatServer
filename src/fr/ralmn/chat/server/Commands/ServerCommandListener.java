package fr.ralmn.chat.server.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class ServerCommandListener extends Thread {

	boolean run = true;
	
	public ServerCommandListener(){
		
	}
	
	
	public void disable(){
		run = false;
	}
	
	public void run(){
		InputStreamReader r = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(r);
		while(run){
			
			try {
				String message = in.readLine();

				CommandManager cm = CommandManager.getInstance();
				
				cm.execute(message, ServerCommand.getInstance());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
