package fr.ralmn.chat.server.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yaml.snakeyaml.error.YAMLException;

import fr.ralmn.chat.server.Util;

public class PluginManager {

	private static PluginManager instance;

	private HashMap<String, ChatPlugin> plugins = new HashMap<String, ChatPlugin>();

	protected final Map<String, Class<?>> classes = new HashMap();
	protected final Map<String, PluginClassLoader> loaders = new LinkedHashMap();

	private PluginManager() {
		instance = this;

	}

	public void reloadPlugins() {

		disablePlugins();

		loadPlugins();

	}
	
	public void reloadPlugin(String name) {
		if(plugins.containsKey(name)){
			plugins.get(name).onEnable();
			plugins.get(name).onDisable();
		}
	}


	public void disablePlugins() {
		for (String s : plugins.keySet()) {
			ChatPlugin plugin = plugins.get(s);
			plugin.onDisable();
			plugins.remove(s);
		}
	}

	public void loadPlugins() {

		File f = new File(Util.getWorkDir(), "plugins/");

		if (!f.exists()) {
			f.mkdir();
		}

		File[] files = f.listFiles();

		for (File fi : files) {

			loadPlugin(fi);

		}

	}

	private void loadPlugin(File fi) {

		PluginDescriptionFile desc = getPluginDescription(fi);

		System.out.println("Tentative de chargement du plugin : "
				+ desc.getName());
		System.out.println("Class Maitresse : " + desc.getMain());
		
		URL[] urls = new URL[1];
		
		try {
			urls[0] = fi.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		PluginClassLoader loader = new PluginClassLoader(this, urls, getClass().getClassLoader());
		
		ChatPlugin plugin = null;
		
		if(loader == null){
			throw new NullPointerException("Loader error");
		}
		
		
		try {
			Class jarClass = Class.forName(desc.getMain(), true, loader);
			Class pluginClass = jarClass.asSubclass(ChatPlugin.class);
			
			Constructor contructor = pluginClass.getConstructor(new Class[0]);
			
			plugin = (ChatPlugin) contructor.newInstance(new Object[0]);
			plugin.initialize(this, desc, loader);
			
			plugins.put(desc.getName(), plugin);
			
			System.out.println("Plugin " + desc.getName() + " chargé");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public PluginDescriptionFile getPluginDescription(File file) {

		JarFile jar = null;
		InputStream stream = null;
		try {
			jar = new JarFile(file);
			JarEntry entry = jar.getJarEntry("plugin.yml");

			if (entry == null) {
				throw new NullPointerException("Plugin.yml Not FOund");
			}

			stream = jar.getInputStream(entry);

			PluginDescriptionFile localPluginDescriptionFile = new PluginDescriptionFile(
					stream);
			return localPluginDescriptionFile;
		} catch (IOException ex) {
		} catch (YAMLException ex) {
		} finally {
			if (jar != null)
				try {
					jar.close();
				} catch (IOException e) {
				}
			if (stream != null)
				try {
					stream.close();
				} catch (IOException e) {
				}
		}

		return null;
	}

	public Class<?> getClassByName(String name) {
		Class cachedClass = (Class) this.classes.get(name);

		if (cachedClass != null) {
			return cachedClass;
		}
		for (String current : this.loaders.keySet()) {
			PluginClassLoader loader = (PluginClassLoader) this.loaders
					.get(current);
			try {
				cachedClass = loader.findClass(name, false);
			} catch (ClassNotFoundException cnfe) {
			}
			if (cachedClass != null) {
				return cachedClass;
			}
		}

		return null;
	}

	public void setClass(String name, Class<?> clazz) {
		if (!this.classes.containsKey(name)) {
			this.classes.put(name, clazz);
		}
	}

	public static PluginManager getInstance() {

		if (instance == null) {
			instance = new PluginManager();
		}
		return instance;
	}

	public Plugin[] getPlugins() {

		Plugin[] plugins = new Plugin[this.plugins.size()];
		
		for(int i = 0; i < this.plugins.keySet().size(); i++){
			String name = (String) this.plugins.keySet().toArray()[i];
			Plugin p = this.plugins.get(name);
			plugins[i] = p;
		
		}
		return plugins;
	}


}
