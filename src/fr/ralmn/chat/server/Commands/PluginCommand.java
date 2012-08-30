package fr.ralmn.chat.server.Commands;

import fr.ralmn.chat.server.Server;
import fr.ralmn.chat.server.Plugin.Plugin;

public class PluginCommand extends Command {

	public PluginCommand() {
		super("plugin", "plugin");
	}

	@Override
	public boolean onCommand(Sender sender, String[] args) {

		Plugin[] plugins = Server.getInstance().getPlugins();
		
		String pl = "Plugins : ";
		
		for(Plugin p : plugins){
			pl += p.getName() + " ";
		}
		
		sender.sendMessageServer(pl);
		
		return true;
	}

	 
	
	
}
