package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.utils.Weight;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public final class LangImpl implements Lang {
	private final String id;
	private final Map<String, Object> values;

	public LangImpl(String name, Map<String, Object> values) {
		this.id = "lang." + name;
		this.values = values;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String get(String key, @Nullable Random r) {
		Object value = values.get(key);
		if (value instanceof Weight<?> weight)
			value = r == null ? weight.get() : weight.get(r);
		return value instanceof String s ? s : key;
	}
}
