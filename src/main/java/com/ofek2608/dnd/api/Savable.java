package com.ofek2608.dnd.api;

public interface Savable {
	Object saveJson();
	void loadJson(Object json);
}
