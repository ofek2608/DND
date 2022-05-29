package com.ofek2608.gim;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class GimBuilder {
	protected final Map<String, String> map;
	protected final String path;

	public GimBuilder() {
		this(new HashMap<>(), "");
	}

	protected GimBuilder(Map<String, String> map, String path) {
		this.map = map;
		this.path = path;
	}

	public GimBuilder child(String subpath) {
		if (subpath.length() == 0)
			return this;

		subpath = subpath.toLowerCase();
		if (!Gim.PATH_VALIDATOR.test(subpath))
			throw new IllegalArgumentException("Illegal path: " + subpath);

		return new GimBuilder(map, path.length() == 0 ? subpath : path + '.' + subpath);
	}

	public GimBuilder child(int index) {
		String subpath = String.format("%04d", index);
		return new GimBuilder(map, path.length() == 0 ? subpath : path + '.' + subpath);
	}

	public GimBuilder root() {
		return new GimBuilder(map, "");
	}

	public GimBuilder rootChild(String path) {
		path = path.toLowerCase();
		if (!Gim.PATH_VALIDATOR.test(path))
			throw new IllegalArgumentException("Illegal path: " + path);

		return new GimBuilder(map, path);
	}





	public void remove() {
		map.remove(path);
	}

	public void set(@Nullable String value) {
		if (value == null)
			map.remove(path);
		else
			map.put(path, value);
	}

	public void set(@Nullable Number value) { set("" + value); }

	public void set(boolean value) {
		set("" + value);
	}

	private void set(String subpath, @Nullable String value) {
		if (subpath.length() == 0)
			set(value);
		else {
			String fullPath = path.length() == 0 ? subpath : path + "." + subpath;
			if (value == null)
				map.remove(fullPath);
			else
				map.put(fullPath, value);
		}
	}









	public void addAll(Map<String,String> map, String subPath) {
		int cutIndex = subPath.length();
		map.forEach((key,value)->{
			if (key.startsWith(subPath))
				set(path + key.substring(cutIndex), value);
		});
	}

	public void addAll(Map<String,String> map) {
		if (path.length() > 0)
			map.forEach((key,value) -> set(path + '.' + key, value));
		else
			map.forEach(this::set);
	}

	public void addAll(GimBuilder builder) {
		addAll(builder.map, builder.path);
	}

	public void addAll(Gim gim, boolean override) {
		int cutIndex = gim.path.length() + 1;
		for (Gim grandChild : gim.getGrandChildren())
			if (override || grandChild.hasValue())
				set(grandChild == gim ? "" : grandChild.path.substring(cutIndex), grandChild.valueString);
	}





	public Map<String, String> buildMap() {
		return new HashMap<>(map);
	}

	public Gim build() {
		return new Gim(map);
	}
}
