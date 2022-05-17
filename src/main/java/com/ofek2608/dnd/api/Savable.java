package com.ofek2608.dnd.api;

import org.jetbrains.annotations.Nullable;

public interface Savable {
	@Nullable Object saveJson();
	void loadJson(@Nullable Object json);
}
