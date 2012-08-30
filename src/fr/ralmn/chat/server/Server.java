package fr.ralmn.chat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import fr.ralmn.chat.server.Commands.User;
import fr.ralmn.chat.server.Plugin.Plugin;
import fr.ralmn.chat.server.Plugin.PluginManager;

/**
 * 
 * Object Serveur contenant toutes les infos du serveur
 * 
 * @author ralmn
 * 
 */
public class Server {

	private static Server instance;
	
	
	private HashMap<String, String> admins = new HashMap<String, String>();
	private HashMap<String, String> vips = new HashMap<String, String>();

	private String ip;
	private int port;
	
	public ArrayList<User> users = new ArrayList<User>();
	
	
	
	/**
	 * 
	 * Private car on aura 2 fois l'object
	 * 
	 */
	private Server() {

		ip = ServerMain.ss.getInetAddress().getHostAddress();
		port = ServerMain.ss.getLocalPort();
		admins = getAdmins();
		vips = getVIP();
		
	}

	public void reloadStaff() {

		admins = getAdmins();
		vips = getVIP();

	}

	private HashMap<String, String> getAdmins() {
		HashMap<String, String> admin = new HashMap<String, String>();
		try {
			File f = new File(Util.getWorkDir(), "/admin.list");

			if (f.exists())
				f.createNewFile();

			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String line = "";

			while ((line = br.readLine()) != null) {
				String[] args = line.split(":");

				admin.put(args[0], args[1]);
			}
			br.close();
			fr.close();

		} catch (Exception e) {
		}
		return admin;
	}

	private HashMap<String, String> getVIP() {
		HashMap<String, String> vip = new HashMap<String, String>();
		try {
			File f = new File(Util.getWorkDir(), "/vip.list");

			if (f.exists())
				f.createNewFile();

			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			String line = "";

			while ((line = br.readLine()) != null) {
				String[] args = line.split(":");

				vip.put(args[0], args[1]);
			}

			br.close();
			fr.close();

		} catch (Exception e) {
		}
		return vip;
	}
	
	
	public static Server getInstance(){
		if(instance == null){
			instance = new Server();
		}
		
		return instance;
		
	}

	public Plugin[] getPlugins() {
		
		return PluginManager.getInstance().getPlugins();
	
	}

	public PluginManager getPluginManager() {
		return PluginManager.getInstance();
	}
	
	

}
