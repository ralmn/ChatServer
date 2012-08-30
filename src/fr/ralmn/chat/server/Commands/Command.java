package fr.ralmn.chat.server.Commands;

import fr.ralmn.chat.server.Server;


public class Command {

	private String cmd;
	
	private String usage;
	
	
	
	public Command(String c, String u) {
		cmd = c;
		usage = u;
		
	}
	
	public boolean onCommand(Sender sender, String[] args){
		return false;
	}

	public void showDescription(Sender sender) {
		sender.sendMessageServer(usage);
	}
	
	public Server getServer(){
		return Server.getInstance();
	}

}
