package fr.ralmn.chat.server.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yaml.snakeyaml.error.YAMLException;

import fr.ralmn.chat.server.Server;

/**
 * 
 * On va utiliser un peux la meme structure que bukkit
 * 
 * @author ralmn
 * 
 */
public class ChatPlugin extends Plugin {

	private ClassLoader classLoader;
	private PluginManager pluginManager;
	
	private PluginDescriptionFile description;
	
	/**
	 * Activation
	 */
	public void onEnable() {
	}

	/**
	 * Desactivation
	 */
	public void onDisable() {
	}

	/**
	 * 
	 * @return Server
	 */
	public Server getServer() {
		return Server.getInstance();
		
	}
	
	
	protected final void initialize(PluginManager pm, PluginDescriptionFile desc, ClassLoader classLoader){
		this.pluginManager = pm;
		this.description = desc;
		this.classLoader = classLoader;
		this.name = desc.getName();
		onEnable();
		
	}

	

}
