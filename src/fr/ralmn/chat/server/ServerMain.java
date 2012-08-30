package fr.ralmn.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import fr.ralmn.chat.server.Commands.BanCommand;
import fr.ralmn.chat.server.Commands.BroadcastCommand;
import fr.ralmn.chat.server.Commands.CommandManager;
import fr.ralmn.chat.server.Commands.KickCommand;
import fr.ralmn.chat.server.Commands.ListCommand;
import fr.ralmn.chat.server.Commands.PluginCommand;
import fr.ralmn.chat.server.Commands.ReloadCommand;
import fr.ralmn.chat.server.Commands.ServerCommandListener;
import fr.ralmn.chat.server.Commands.User;
import fr.ralmn.chat.server.Commands.UserCommand;
import fr.ralmn.chat.server.Plugin.PluginManager;

public class ServerMain {

	//public static ArrayList<User> users = new ArrayList<User>();
	public static ServerSocket ss;
	private static Thread t;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Util.initFile();
		
		
		setCloseAction();
		
		
		registerCommands();
		
		int port = Util.getPort();
		try {
			ss = new ServerSocket(30000);
		} catch (IOException e) {
			System.err.println("Le port " + port + " est déjà utilisé !");
			return;
		}
		System.out.println("Le serveur est à l'écoute du port "
				+ ss.getLocalPort());

		t = new Thread(new Login(ss));
		t.start();
		
		ServerCommandListener serverCommandListener = new ServerCommandListener();
		serverCommandListener.start();
		
		PluginManager pm = PluginManager.getInstance();
		
		pm.loadPlugins();
		
	}
	private static void setCloseAction() {
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            System.out.println("In shutdown hook");
	        }
	    }, "Shutdown-thread"));
		
	}
	private static void registerCommands() {

		CommandManager cm = CommandManager.getInstance();
		cm.registerCommand("kick", new KickCommand());
		cm.registerCommand("ban", new BanCommand());
		cm.registerCommand("list", new ListCommand());
		cm.registerCommand("broadcast", new BroadcastCommand());
		cm.registerCommand("user", new UserCommand());
		cm.registerCommand("plugin", new PluginCommand());
		cm.registerCommand("reload", new ReloadCommand());
		
	}
	public static boolean isConnecting(String name) {

		return (User.getUser(name) != null);
	}

	public static void d(User u) {
		if (Server.getInstance().users.contains(u)) {
			Server.getInstance().users.remove(u);
		} else {
		}

	}

}
