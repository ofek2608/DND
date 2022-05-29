package com.ofek2608.dnd.resources;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

interface ResourceLoader {
	@Nullable
	Object load(LoadContext ctx);




	@SuppressWarnings("unchecked")
	static ResourceLoader getLoader(Class<?> clazz) {
		if (clazz.isEnum())
			return new EnumLoader((Class<? extends Enum<?>>)clazz);
		if (clazz.isArray())
			return new ArrayLoader(clazz.componentType());
		try {
			return new ObjectLoader(clazz);
		} catch (IllegalArgumentException ignored) {}
		if (clazz == String.class)
			return STRING_LOADER;
		if (clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class ||
				clazz == float.class || clazz == double.class || clazz == boolean.class)
			return NUMBER_LOADER;

		return UNKNOWN_LOADER;
	}


	ResourceLoader STRING_LOADER = ctx -> ctx.data.valueString;
	ResourceLoader NUMBER_LOADER = ctx -> ctx.data.valueNumber;
	ResourceLoader UNKNOWN_LOADER = ctx -> ctx;


	class ArrayLoader implements ResourceLoader {
		private final Class<?> clazz;

		public ArrayLoader(Class<?> clazz) {
			this.clazz = clazz;
		}

		@Override
		public Object load(LoadContext ctx) {
			return ctx.data.childrenStream()
					.map(ctx::of)
					.map(subCtx->ctx.manager.load(clazz, subCtx))
					.toArray(size->(Object[]) Array.newInstance(clazz, size));
		}
	}


	class EnumLoader implements ResourceLoader {
		private final Map<String,Object> values;

		public EnumLoader(Class<? extends Enum<?>> clazz) {
			Enum<?>[] enumConstants = clazz.getEnumConstants();
			values = new HashMap<>();
			for (Enum<?> val : enumConstants) {
				values.put(val.name().toLowerCase(), val);
			}
		}

		@Nullable
		@Override
		public Object load(LoadContext ctx) {
			@Nullable
			String key = ctx.data.valueString;
			return key == null ? null : values.get(key);
		}
	}

	class ObjectLoader implements ResourceLoader {
		private final Constructor<?> constructor;

		public ObjectLoader(Class<?> clazz) {
			try {
				this.constructor = clazz.getConstructor(LoadContext.class);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("Couldn't find constructor(LoadContext) for " + clazz);
			}
		}

		@Nullable
		@Override
		public Object load(LoadContext ctx) {
			try {
				return constructor.newInstance(ctx);
			} catch (Exception e) {
				if (e instanceof InvocationTargetException ite)
					if (ite.getTargetException() instanceof LoadNullException)
						return null;
				System.out.println("Error while loading " + ctx.data.path);
				e.printStackTrace();
				return null;
			}
		}
	}
}
