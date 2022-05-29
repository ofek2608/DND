package com.ofek2608.utils;

import com.ofek2608.annotation.AllNonnullByDefault;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@AllNonnullByDefault
public final class ReflectionUtils {
	private ReflectionUtils() {}

	public static void setField(Field field, Object inst, Object val) throws IllegalAccessException {
		field.setAccessible(true);
		setNonFinal(field);
		field.set(inst, val);
	}

	public static void setNonFinal(Field field) {
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		} catch (Exception ignored) {}
	}
}
