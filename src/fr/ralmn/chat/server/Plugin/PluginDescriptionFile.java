package fr.ralmn.chat.server.Plugin;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class PluginDescriptionFile {

	private static final Yaml yaml = new Yaml(new SafeConstructor());
	private String name;
	private String main = null;
	private String version;
	
	
	public PluginDescriptionFile(InputStream stream){
		loadMap((Map)yaml.load(stream));
	}


	private void loadMap(Map<?, ?> map){
		this.name = map.get("name").toString();
		this.main = map.get("main").toString();
		this.version = map.get("version").toString();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMain() {
		return main;
	}


	public void setMain(String main) {
		this.main = main;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	

}
