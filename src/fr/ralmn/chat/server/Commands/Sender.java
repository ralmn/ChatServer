package fr.ralmn.chat.server.Commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender {
	
	protected String user;

	public Sender(String user){
		this.user = user;
	}
	
	
	public final String getUser(){
		return user;
	}
	
	public void sendMessage(User u, String m) {
			
			m = m.replace("<", "&lt;");
			m = m.replace(">", "&gt;");
			
			String msg = u.getDisplayName() + " : " + m;
			System.out.println(msg);
	}
	
	
	public void sendMessageServer(String m) {
			String msg = "Serveur"+  ": " + m;
			System.out.println(msg);
	}


	public boolean isAdmin() {
		return true;
	}
	
	
}
