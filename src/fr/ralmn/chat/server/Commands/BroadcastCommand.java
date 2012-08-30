package fr.ralmn.chat.server.Commands;

import java.io.PrintWriter;

import fr.ralmn.chat.server.Server;
import fr.ralmn.chat.server.ServerMain;

public class BroadcastCommand extends Command {

	public BroadcastCommand(){
		super("broadcast", "broadcast [message]");
	}
		
	@Override
	public boolean onCommand(Sender sender, String[] args) {
		
		if(!sender.isAdmin()){
			sender.sendMessageServer("Tu n'a pas la permisions");
			return true;
		}
		
		String b = "";
		for (int i = 0; i < args.length; i++) {
			b += args[i] + " ";
		}
		System.out.println(b);
		for (User u : Server.getInstance().users) {

			PrintWriter out = u.getOut();
			
			String t = " <b><font color=#00BB45> [BROADCAST] : </font> <font color=#F20000> "
					+ b + "</font> </b>";
			out.println(t);
			out.flush();

		}
		return true;
	}

	
	
}
