package de.paxii.clarinet.util.reflection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ReflectionHelper {

  /**
   * @param packageName The package name to search in
   * @param superClass  The class to get child classes of
   * @param <T>         Type of super class
   * @return Array of matching classes
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<? extends T>[] getClassesInPackageBySuperType(String packageName, Class<T> superClass)
          throws ClassNotFoundException, IOException, URISyntaxException {
    return Stream.of(ReflectionHelper.getClasses(packageName))
            .filter(foundClass -> foundClass.getSuperclass().equals(superClass))
            .toArray(Class[]::new);
  }

  /**
   * Scans all classes accessible from the context class loader which belong to the given package
   * and subpackages.
   *
   * @param packageName The base package
   * @return The classes
   */
  public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    URL url = ReflectionHelper.class.getProtectionDomain().getCodeSource().getLocation();
    Path dir = Paths.get(url.toURI());
    ArrayList<Class> classes = new ArrayList<>();

    if (Files.isDirectory(dir)) {
      String path = packageName.replace('.', '/');
      Enumeration<URL> resources = classLoader.getResources(path);
      List<File> dirs = new ArrayList<>();

      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        dirs.add(new File(resource.getFile()));
      }

      for (File directory : dirs) {
        classes.addAll(findClasses(directory, packageName));
      }
    } else {
      JarFile jarFile;

      try {
        jarFile = new JarFile(dir.toFile());
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
          JarEntry entry = entries.nextElement();
          String name = entry.getName();

          if (name.endsWith(".class")) {
            String className = name.replaceAll("/", ".");
            className = className.substring(0, className.length() - 6);

            if (className.contains(packageName)) {
              Class<?> clazz = classLoader.loadClass(className);
              classes.add(clazz);
            }
          }
        }
      } catch (Throwable exception) {
        exception.printStackTrace();
      }
    }

    return classes.toArray(new Class[classes.size()]);
  }

  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory   The base directory
   * @param packageName The package name for classes found inside the base directory
   * @return The classes
   */
  private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<>();
    File[] files;

    if (!directory.exists() || (files = directory.listFiles()) == null) {
      return classes;
    }

    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
      }
    }

    return classes;
  }

}
