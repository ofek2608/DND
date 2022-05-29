package com.ofek2608.dnd.resources;

public interface IGameData {
	String getPath();

	default String getName() {
		String path = getPath();
		return path.substring(path.lastIndexOf('.') + 1);
	}
}
