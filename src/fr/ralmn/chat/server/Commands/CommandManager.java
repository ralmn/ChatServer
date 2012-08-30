package fr.ralmn.chat.server.Commands;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import fr.ralmn.chat.server.Server;
import fr.ralmn.chat.server.ServerMain;

public class CommandManager {

	private static CommandManager instance;
	private HashMap<String, Command> commands = new HashMap<String, Command>();

	
	public CommandManager(){
		
	}
	
	public boolean registerCommand(String name, Command cmd){
		return commands.put(name, cmd) != null;
	}
	
	private boolean executeCommand(String cmd, Sender sender, String[] args){
		if(commands.containsKey(cmd)){
			Command command = commands.get(cmd);
			
			if(command.onCommand(sender, args)){
				return true;
			}else{
				command.showDescription(sender);
				return false;
			}
		}else{
			NotFound(sender, cmd);
			return false;
		}
		
	}
		
	private void NotFound(Sender sender, String cmd){
		sender.sendMessageServer("La commande " + cmd + " n'a pas été trouver");
	}
	
	
	
	public static boolean check(String cmd, User u) {

		String[] args = cmd.split(" ");

		if (!u.isAdmin()) {

			u.getOut().println("Tu n'a pas l'acces au commands");
			u.getOut().flush();
			return true;
		} else if (args[0].equalsIgnoreCase("/kick") && args.length == 2) {
			kick(args[1]);
			return true;
		} else if (args[0].equalsIgnoreCase("/list")) {
			String name = "Personne connecté : ";

			for (User us : Server.getInstance().users) {
				name += us.getDisplayName() + " ";
			}

			u.getOut().println(name);
			u.getOut().flush();
			return true;
		} else if (args[0].equalsIgnoreCase("/ban")) {

			ban(args[1], u);
			return true;

		} else if (args[0].equalsIgnoreCase("/unban")) {

			unban(args[1], u);
			return true;

		} else if (args[0].equalsIgnoreCase("/user") && args.length == 4) {

			return false;

		} else if (args[0].equalsIgnoreCase("/broadcast")) {
			String b = "";
			for (int i = 1; i < args.length; i++) {
				b += args[i] + " ";
			}
			broadcast(b);
			return true;

		} else {
			return false;
		}

	}

	private static void unban(String string, User us) {
		User u = User.getUser(string);
		if (u != null)
			u.unban();
		else {
			User.setBanned(false, string);
			us.getOut().println(
					"Le joueur " + string + " viens d'etre debannis ! ");
			us.getOut().flush();
		}
	}

	private static void ban(String string, User us) {
		User u = User.getUser(string);
		if (u != null)
			u.ban();
		else {
			User.setBanned(true, string);
			us.getOut().println(
					"Le joueur " + string + " viens d'etre bannis ! ");
			us.getOut().flush();
		}

	}

	private static void kick(String string) {
		User u = User.getUser(string);
		if (u != null)
			u.kick();

	}

	private static void broadcast(String b) {

		for (User u : Server.getInstance().users) {

			PrintWriter out = u.getOut();

			String t = " <b><font color=#00BB45> [BROADCAST] : </font> <font color=#F20000> "
					+ b + "</font> </b>";
			out.println(t);
			out.flush();

		}

	}

	public static CommandManager getInstance() {
		if(instance == null){
			instance = new CommandManager();
		}
		return instance;
	}

	public boolean execute(String message, Sender sender) {
		String[] a = message.split(" ");

		
		ArrayList<String> aa = new ArrayList<String>(Arrays.asList(a));
		String cmd = aa.remove(0);
		cmd = a[0].replace("/", "");
		
		String args[] = new String[aa.size()];
		
		 args = aa.toArray(args);
		 
		boolean result = executeCommand(cmd, sender, args);
		
		return result;
	}
	
	
}
