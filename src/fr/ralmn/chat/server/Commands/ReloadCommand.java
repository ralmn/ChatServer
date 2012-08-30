package fr.ralmn.chat.server.Commands;

public class ReloadCommand extends Command {

	public ReloadCommand() {
		super("reload", "reload <plugin>");
	}

	@Override
	public boolean onCommand(Sender sender, String[] args) {
		
		if(args.length < 1)
		getServer().getPluginManager().reloadPlugins();
		else{
			getServer().getPluginManager().reloadPlugin(args[0]);
		}
		
	
		return true;
	}

	 
	
	
}
