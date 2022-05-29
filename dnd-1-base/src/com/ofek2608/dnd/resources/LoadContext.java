package com.ofek2608.dnd.resources;

import com.ofek2608.gim.Gim;

/**
 * Resource Loading Context
 */
public final class LoadContext {
	public final ResourceLoadManager manager;
	public final GameRegistry registry;
	public final Gim data;

	public LoadContext(GameRegistry registry, Gim data) {
		this(new ResourceLoadManager(), registry, data);
	}

	private LoadContext(ResourceLoadManager manager, GameRegistry registry, Gim data) {
		this.manager = manager;
		this.registry = registry;
		this.data = data;
	}

	public LoadContext of(Gim gim) {
		return new LoadContext(manager, registry, gim);
	}

	public LoadContext get(String key) {
		return of(data.get(key));
	}

	public LoadContext get(int index) {
		return of(data.getChild(index));
	}

	public <T> T load(Class<T> clazz) {
		return manager.load(clazz, this);
	}
}
