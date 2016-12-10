package de.paxii.clarinet.util.module.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager {
  public static void copyFileFromJar(final String pathToFile,
                                     final File newFile) {
    new Thread(() -> {
      InputStream stream = FileManager.class
              .getResourceAsStream(pathToFile);

      if (stream == null) {
        stream = FileManager.class.getResourceAsStream(pathToFile);

        if (stream == null) {
          return;
        }
      }

      OutputStream resStreamOut;
      int readBytes;
      byte[] buffer = new byte[4096];

      try {
        resStreamOut = new FileOutputStream(newFile);

        while ((readBytes = stream.read(buffer)) > 0) {
          resStreamOut.write(buffer, 0, readBytes);
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }).start();
  }
}
