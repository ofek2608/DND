package com.ofek2608.dnd.resources;

import com.ofek2608.gim.Gim;
import com.ofek2608.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

final class ResourceLoadManager {
	private final Map<Class<?>, ResourceLoader> loaders = new HashMap<>();
	private final Map<Class<?>, Class<?>[]> subclasses = new HashMap<>();

	private Class<?>[] getSubclasses(Class<?> clazz) {
		//TODO add priority for classes with annotation
		if (clazz.isArray() || clazz.isPrimitive())
			return new Class[]{clazz};


		Set<Class<?>> subclasses = new HashSet<>();
		Queue<Class<?>> queue = new ArrayDeque<>();
		queue.add(clazz);
		while (!queue.isEmpty()) {
			Class<?> c = queue.poll();
			int modifiers = c.getModifiers();

			if (!c.isInterface() && !Modifier.isAbstract(modifiers))
				subclasses.add(c);

			if (Modifier.isFinal(modifiers))
				continue;

			if (c.isSealed())
				queue.addAll(Arrays.asList(c.getPermittedSubclasses()));
		}

		return subclasses.toArray(new Class<?>[0]);
	}

	@SuppressWarnings({"ConstantConditions", "unchecked"})
	<T> T load(Class<T> clazz, LoadContext ctx) {
		Class<?>[] subclasses = this.subclasses.computeIfAbsent(clazz, this::getSubclasses);
		Class<?> subclass = getValidSubClass(subclasses, ctx.data);
		ResourceLoader loader = this.loaders.computeIfAbsent(subclass, ResourceLoader::getLoader);
		return (T)loader.load(ctx);
	}

	private Class<?> getValidSubClass(Class<?>[] subclasses, Gim data) {
		for (Class<?> subclass : subclasses) {
			if (isValid(subclass, data))
				return subclass;
		}
		throw new RuntimeException("Couldn't find valid subclass.");
	}

	private static boolean isValid(Class<?> clazz, Gim data) {
		try {
			return (boolean) clazz.getMethod("validateLoad", Gim.class).invoke(null, data);
		} catch (Exception e) {
			return true;
		}
	}

	private final Map<Class<?>, Field[]> classFields = new ConcurrentHashMap<>();
	{
		classFields.put(LoadableGameData.class, new Field[0]);
		classFields.put(Object.class, new Field[0]);
	}
	Field[] getFields(Class<?> clazz) {
		Field[] result = classFields.get(clazz);

		if (result == null) {
			Field[] thisClass = Stream.of(clazz.getDeclaredFields())
					.filter(f -> f.getAnnotation(Res.class) != null)
					.peek(f -> {
						f.setAccessible(true);
						ReflectionUtils.setNonFinal(f);
					}).toArray(Field[]::new);
			Field[] superClass = getFields(clazz.getSuperclass());

			result = new Field[thisClass.length + superClass.length];
			for (int i = 0; i < thisClass.length; i++)
				result[i] = thisClass[thisClass.length - i - 1];
			System.arraycopy(superClass, 0, result, thisClass.length, superClass.length);

			classFields.put(clazz, result);
		}

		return result;
	}
}
