package de.paxii.clarinet.module.external;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.settings.ClientSettings;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;

public class ExternalModuleLoader {
  private final File moduleFolder;

  public ExternalModuleLoader() {
    this.moduleFolder = new File((ClientSettings.getClientFolderPath().getValue() + "/modules"));
    this.moduleFolder.mkdirs();
  }

  public void loadModules() {
    this.loadModules(null);
  }

  public void loadModules(Runnable callback) {
    new Thread(() -> {
      try {
        this.loadModuleJars(this.getModuleJars());
        Module[] externalModules = this.loadExternalModules();

        for (Module externalModule : externalModules) {
          externalModule.setPlugin(true);
          Wrapper.getModuleManager().addModule(externalModule);
        }
      } catch (Exception x) {
        x.printStackTrace();
      } finally {
        if (callback != null) {
          callback.run();
        }
      }
    }).start();
  }

  private File[] getModuleJars() {
    ArrayList<File> moduleJars = new ArrayList<>();
    moduleJars.addAll(Arrays.asList(this.moduleFolder.listFiles((f) -> f.getName().endsWith(".jar"))));
    return moduleJars.toArray(new File[moduleJars.size()]);
  }

  private void loadModuleJars(File[] moduleJars) {
    for (File moduleFile : moduleJars) {
      this.addToClassPath(moduleFile);
    }
  }

  private Module[] loadExternalModules() {
    ArrayList<Module> moduleList = new ArrayList<>();

    Iterator<Module> it = ServiceLoader.load(Module.class).iterator();

    while (it.hasNext()) {
      try {
        moduleList.add(it.next());
      } catch (Exception x) {
        x.printStackTrace();
      }
    }

    return moduleList.toArray(new Module[moduleList.size()]);
  }

  private void addToClassPath(File file) {
    URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    Class systemClassLoaderClass = URLClassLoader.class;

    try {
      Method method = systemClassLoaderClass.getDeclaredMethod("addURL", URL.class);
      method.setAccessible(true);
      method.invoke(systemClassLoader, file.toURI().toURL());
    } catch (Exception e) {
      System.out.println("Could not load Module " + file.getName());
      e.printStackTrace();
    }

  }
}
