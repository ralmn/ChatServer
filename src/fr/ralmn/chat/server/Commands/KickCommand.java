package fr.ralmn.chat.server.Commands;


public class KickCommand extends Command {

	public KickCommand() {
		super("kick", "kick [user]");
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
				u.kick();
				return true;
			}
		}
		return false;
	}

}
