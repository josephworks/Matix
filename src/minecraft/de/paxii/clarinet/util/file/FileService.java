package de.paxii.clarinet.util.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Scanner;

public class FileService {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public static void setFileContents(File file, String fileContents) throws IOException {
    if (!file.exists()) {
      file.createNewFile();
    }

    PrintWriter printWriter = new PrintWriter(file);
    printWriter.print(fileContents);
    printWriter.close();
  }

  public static void setFileContentsAsJson(File file, Object object) throws IOException {
    String json = gson.toJson(object);

    FileService.setFileContents(file, json);
  }

  public static String getFileContents(File file) throws IOException {
    Scanner scanner = new Scanner(file);
    StringBuilder stringBuilder = new StringBuilder();
    while (scanner.hasNextLine()) {
      stringBuilder.append(scanner.nextLine());
    }
    scanner.close();

    return stringBuilder.toString();
  }

  public static <T> T getFileContents(File file, Type type) throws IOException, JsonSyntaxException {
    String fileContents = FileService.getFileContents(file);

    return gson.fromJson(fileContents, type);
  }

  public static <T> T getFileContents(File file, Class<T> type) throws IOException, JsonSyntaxException {
    return FileService.getFileContents(file, (Type) type);
  }

}
