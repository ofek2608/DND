package com.ofek2608.utils;

import javax.annotation.Nullable;

public final class MathUtils {
	private MathUtils() {}

	public static int parsePositiveInt(@Nullable String str) {
		if (str == null)
			return -1;
		try {
			return Math.max(Integer.parseInt(str), -1);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static @Nullable Number parseString(@Nullable String str) {
		if (str == null)
			return null;
		try { return Long  .parseLong  (str); } catch (NumberFormatException ignored) {}
		try { return Double.parseDouble(str); } catch (NumberFormatException ignored) {}
		return switch (str) {
			case "true" -> 1;
			case "false" -> 0;
			default -> null;
		};
	}
}
