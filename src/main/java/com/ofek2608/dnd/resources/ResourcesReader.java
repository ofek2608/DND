package com.ofek2608.dnd.resources;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public final class ResourcesReader {
	private ResourcesReader() {}

	public static byte[] getBytes(String path) {
		InputStream stream = ResourcesReader.class.getClassLoader().getResourceAsStream(path);

		if (stream == null)
			return new byte[0];

		try {
			return stream.readAllBytes();
		} catch (IOException e) {
			System.err.println("Couldn't load resource from path '" + path + "'");
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static String getString(String path) {
		return new String(getBytes(path), StandardCharsets.UTF_8);
	}

	public static Object getJson(String path) {
		try {
			return new JSONParser().parse(getString(path));
		} catch (ParseException e) {
			System.err.println("Couldn't parse json from path '" + path + "'");
			e.printStackTrace();
			return Collections.EMPTY_MAP;
		}
	}
}
