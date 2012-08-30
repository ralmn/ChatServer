package fr.ralmn.chat.server.Commands;


public class UserCommand extends Command {

	public UserCommand() {
		super("user", "");
	}

	@Override
	public boolean onCommand(Sender sender, String[] args) {
		
		if(!sender.isAdmin()){
			sender.sendMessageServer("Tu n'a pas la permisions");
			return true;
		}
		
		if (args.length >= 4) {

			if (args[0].equalsIgnoreCase("admin")) {

				if (args[1].equalsIgnoreCase("rem")) {
					remAd(args[2], sender);
					return true;
				} else if (args[1].equalsIgnoreCase("add")) {
					addAd(args[2], sender);
					return true;
				} else
					return false;

			} else if (args[0].equalsIgnoreCase("vip")) {

				if (args[1].equalsIgnoreCase("rem")) {
					remVip(args[2], sender);
					return true;
				} else if (args[1].equalsIgnoreCase("add")) {
					addVip(args[2], sender);
					return true;
				} else
					return false;

			} else
				return false;
		} else {
			return false;
		}

	}
	

	private static void remVip(String a, Sender u) {
		User us = User.getUser(a);
		if (us != null) {
			us.remVip();
		}
	}

	private static void addVip(String a, Sender u) {
		User us = User.getUser(a);
		if (us != null) {
			us.addVip();
		}
	}

	private static void remAd(String a, Sender u) {
		User us = User.getUser(a);
		if (us != null) {
			us.remAdmin();
		}
	}

	private static void addAd(String a, Sender u) {
		User us = User.getUser(a);
		if (us != null) {
			us.addAdmin();
		}
	}


}
