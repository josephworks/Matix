package de.paxii.clarinet.util.web;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by Lars on 05.06.2016.
 */
public class JsonFetcher {
	private static final Gson gson = new Gson();

	@Deprecated
	public static <T> T fetchData(String endPoint, Type type) {
		try {
			Scanner scanner = new Scanner(new InputStreamReader(new URL(endPoint).openStream()));
			StringBuilder stringBuilder = new StringBuilder();

			while (scanner.hasNextLine()) {
				stringBuilder.append(scanner.nextLine());
			}

			scanner.close();

			return gson.fromJson(stringBuilder.toString(), type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Deprecated
	public static <T> T fetchData(String endPoint, Class<T> type) {
		return JsonFetcher.fetchData(endPoint, (Type) type);
	}

	public static <T> T get(String endPoint, Class<T> type) {
		return JsonFetcher.request(endPoint, "GET", "", type, new HashMap<>());
	}

	public static <T> T post(String endPoint, String body, Class<T> type) {
		return JsonFetcher.request(endPoint, "POST", body, type, new HashMap<>());
	}

	public static <T> T post(String endPoint, String body, Class<T> type, HashMap<String, String> requestProperties) {
		return JsonFetcher.request(endPoint, "POST", body, type, requestProperties);
	}

	/**
	 * @param endPoint URL to send the request to
	 * @param type     Type to cast the data to
	 * @param method   HTTP Method to use for the request
	 * @param <T>
	 * @return T
	 */
	private static <T> T request(String endPoint, String method, String body, Type type, HashMap<String, String> properties) {
		StringBuilder stringBuilder = new StringBuilder();

		try {
			URL url = new URL(endPoint);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod(method);
			urlConnection.setRequestProperty("Accept", "application/json,text/plain");
			urlConnection.setRequestProperty("User-Agent", UUID.randomUUID().toString());
			properties.forEach(urlConnection::setRequestProperty);

			urlConnection.setDoOutput(true);

			if (body.length() > 0 && !method.equals("GET") || !method.equals("DELETE")) {
				DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
				dataOutputStream.writeBytes(body);
				dataOutputStream.flush();
				dataOutputStream.close();
			}

			Scanner scanner = new Scanner(new InputStreamReader(urlConnection.getInputStream()));

			while (scanner.hasNextLine()) {
				stringBuilder.append(scanner.nextLine());
			}

			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (type != String.class) {
			try {
				return gson.fromJson(stringBuilder.toString(), type);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
		} else {
			return (T) stringBuilder.toString();
		}

		return null;
	}
}
