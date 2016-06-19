package de.paxii.clarinet.util.web;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;

/**
 * Created by Lars on 05.06.2016.
 */
public class JsonFetcher {
	private static final Gson gson = new Gson();

	public static <T> T fetchData(String endPoint, Type type) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new URL(endPoint).openStream()));
			String contents = "", line;

			while ((line = br.readLine()) != null) {
				contents += line;
			}

			br.close();

			return gson.fromJson(contents, type);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> T fetchData(String endPoint, Class<T> type) {
		return JsonFetcher.fetchData(endPoint, (Type) type);
	}
}
