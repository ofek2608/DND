package com.ofek2608.dnd.resources;

public class GameData implements IGameData {
	public final String path;
	public final String name;

	public GameData(GameRegistry registry, String path) {
		this(path);
		registry.register(this);
	}

	public GameData(String path) {
		this.path = path;
		this.name = path.substring(path.lastIndexOf('.') + 1);
	}

	@Override
	public final String getPath() {
		return path;
	}

	@Override
	public final String getName() {
		return name;
	}
}
