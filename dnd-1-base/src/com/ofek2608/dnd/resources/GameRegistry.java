package com.ofek2608.dnd.resources;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class GameRegistry {
	private final Map<String, IGameData> objs0 = new HashMap<>();
	public final Map<String, IGameData> objs = Collections.unmodifiableMap(objs0);

	public void register(IGameData... objects) {
		for (IGameData object : objects)
			objs0.put(object.getPath(), object);
	}

	public void registerAll(GameRegistry other) {
		objs0.putAll(other.objs0);
	}

	@Contract("null->null")
	@Nullable
	public IGameData get(@Nullable String id) {
		return objs0.get(id);
	}
}
