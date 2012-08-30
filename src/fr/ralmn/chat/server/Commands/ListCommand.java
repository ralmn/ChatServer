package fr.ralmn.chat.server.Commands;

import fr.ralmn.chat.server.Server;
import fr.ralmn.chat.server.ServerMain;

public class ListCommand extends Command {

	public ListCommand() {
		super("list", "list");
	}

	@Override
	public boolean onCommand(Sender sender, String[] args) {
		String name = "Personne connect� : ";

		for (User us : Server.getInstance().users) {
			name += us.getDisplayName() + " ";
		}

		sender.sendMessageServer(name);
		return true;
	}

	
	
}
