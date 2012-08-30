package fr.ralmn.chat.server.Commands;

public class ServerCommand  extends Sender{

	private static ServerCommand instance;
	
	private ServerCommand() {
		super("Server");
		instance = this;
	}

	
	public static ServerCommand getInstance(){
		if(instance == null){
			instance = new ServerCommand();
		}
		return instance;
	}
	
}
