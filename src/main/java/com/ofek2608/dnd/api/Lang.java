package com.ofek2608.dnd.api;

import javax.annotation.Nullable;
import java.util.Random;

public interface Lang extends Identifiable {
	String get(String key, @Nullable Random r);

	default String get(String key) {
		return get(key, null);
	}

	default String getName() {
		return get("properties.name");
	}
}
