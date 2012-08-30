package fr.ralmn.chat.server.Commands;


public class BanCommand extends Command {

	public BanCommand() {
		super("ban", "ban [user]");
	}
	
	
	@Override
	public boolean onCommand(Sender sender, String[] args) {
		
		if(!sender.isAdmin()){
			sender.sendMessageServer("Tu n'a pas la permisions");
			return true;
		}
		
		if(args.length >= 1){		
			User u = User.getUser(args[0]);
			if (u != null && !u.equals(sender) && !u.isAdmin()){
				u.ban();
				return true;
			}
		}
		return false;
		
		
	}

}
