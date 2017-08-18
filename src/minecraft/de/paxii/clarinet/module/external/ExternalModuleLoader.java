package de.paxii.clarinet.module.external;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.module.store.ModuleStore;
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
    this.moduleFolder = ModuleStore.getModuleFolder();
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
    ArrayList<File> moduleJars = new ArrayList<>(Arrays.asList(this.moduleFolder.listFiles((f) -> f.getName().endsWith(".jar"))));
    ArrayList<File> ignoredModules = new ArrayList<>();

    // Filter duplicated modules
    return moduleJars.stream().filter(moduleJar -> {
      if (!ignoredModules.contains(moduleJar)) {
        for (File otherModuleJar : moduleJars) {
          if (moduleJar.getName().equals(otherModuleJar.getName()) ||
                  ignoredModules.contains(otherModuleJar)) {
            continue;
          }

          String[] moduleJarName = moduleJar.getName().split("-");
          String[] otherModuleJarName = otherModuleJar.getName().split("-");

          if (moduleJarName[0].equals(otherModuleJarName[0])) {
            ignoredModules.add(moduleJar);
            ModuleStore.getModulesToDelete().add(moduleJar.getName().replace(".jar", ""));
            return false;
          }
        }
      }

      return true;
    }).toArray(File[]::new);
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
