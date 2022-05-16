package com.ofek2608.dnd.resources;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.impl.LangImpl;

import java.util.HashMap;
import java.util.Map;

public final class Resources {
	private Resources() {}
	private static final Map<String, Identifiable> IDENTIFIABLES = new HashMap<>();

	public static Identifiable get(String id) {
		return IDENTIFIABLES.get(id);
	}

	public static void setPackages(ResourcePackage ... packages) {
		IDENTIFIABLES.clear();

		Map<String, Map<String,Object>> languages = new HashMap<>();
		for (ResourcePackage pack : packages) {
			IDENTIFIABLES.putAll(pack.identifiables);
			//add all ${pack.languagesMap} to ${languages}
			pack.languagesMap.forEach((name, map) -> languages.computeIfAbsent(name, a->new HashMap<>()).putAll(map));
		}

		languages.forEach((name, map)->{
			LangImpl lang = new LangImpl(name, map);
			IDENTIFIABLES.put(lang.getId(), lang);
		});
	}

	public static void register(Identifiable ... objects) {
		for (Identifiable object : objects)
			IDENTIFIABLES.put(object.getId(), object);
	}
}
