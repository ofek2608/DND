package com.ofek2608.utils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class ResourcesUtils {
	private ResourcesUtils() {}

	public static byte[] getBytes(String path) {
		@Nullable
		InputStream stream = ResourcesUtils.class.getClassLoader().getResourceAsStream(path);

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
}
