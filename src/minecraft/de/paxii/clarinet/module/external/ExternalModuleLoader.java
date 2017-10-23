package de.paxii.clarinet.module.external;

import de.paxii.clarinet.Wrapper;
import de.paxii.clarinet.module.Module;
import de.paxii.clarinet.util.module.store.ModuleStore;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Module[] externalModules = this.loadExternalModules(this.getModuleJars());
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

  private Module[] loadExternalModules(File[] moduleJars) {
    ArrayList<Module> moduleList = new ArrayList<>();
    Iterator<Module> it = ServiceLoader.load(Module.class, this.getPluginClassLoader(moduleJars)).iterator();

    while (it.hasNext()) {
      try {
        moduleList.add(it.next());
      } catch (Exception x) {
        x.printStackTrace();
      }
    }

    return moduleList.toArray(new Module[moduleList.size()]);
  }

  private ClassLoader getPluginClassLoader(File[] jarFiles) {
    Function<File, URL> toUrl = file -> {
      try {
        return file.toURI().toURL();
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
      return null;
    };

    return URLClassLoader.newInstance(
            Arrays.stream(jarFiles).map(toUrl).collect(Collectors.toList()).toArray(new URL[jarFiles.length])
    );
  }

}
