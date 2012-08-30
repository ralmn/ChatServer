package fr.ralmn.chat.server.Plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class PluginClassLoader extends URLClassLoader
{
  private final PluginManager loader;
  private final Map<String, Class<?>> classes = new HashMap();

  public PluginClassLoader(PluginManager loader, URL[] urls, ClassLoader parent) {
    super(urls, parent);

    this.loader = loader;
  }

  public void addURL(URL url)
  {
    super.addURL(url);
  }

  protected Class<?> findClass(String name) throws ClassNotFoundException
  {
    return findClass(name, true);
  }

  protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
    Class result = (Class)this.classes.get(name);

    if (result == null) {
      if (checkGlobal) {
        result = this.loader.getClassByName(name);
      }

      if (result == null) {
        result = super.findClass(name);

        if (result != null) {
          this.loader.setClass(name, result);
        }
      }

      this.classes.put(name, result);
    }

    return result;
  }

  public Set<String> getClasses() {
    return this.classes.keySet();
  }
}