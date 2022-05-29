package com.ofek2608.gim_old;

import com.ofek2608.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.*;

/*
 * TODO complete rewrite
 *  - need the references to clear itself
 *  - need functions to get stuff directly, without references
 *  - need to throw exception for illegal child path (case is okay)
 *  - need to be efficient (maybe use buckets)
 */
public class SimpleGim implements Gim {
	private final Map<String, SimpleGimValue> valueMap = new HashMap<>();
	private final Map<String, Children> childrenMap = new HashMap<>();
	private final Map<String, GimRef> references = new HashMap<>();

	private final GimRef rootRef = new GimRef("", "", 0, null);

	private void remove(String id) {
		valueMap.remove(id);
		@Nullable
		Children children = childrenMap.get(id);
		while (children != null && children.children.isEmpty()) {
			childrenMap.remove(children.id);
			children = children.parentChildren;
		}
	}

	private Children createChildren(String id) {
		Children children = childrenMap.get(id);
		if (children != null)
			return children;
		int dotIndex = id.lastIndexOf('.');
		@Nullable
		Children parentChildren = id.length() == 0 ? null : createChildren(id.substring(dotIndex + 1));
		children = new Children(id, parentChildren);
		childrenMap.put(id, children);

		if (parentChildren != null)
			parentChildren.children.add(dotIndex < 0 ? id : id.substring(0, dotIndex));

		return children;
	}

	private SimpleGimValue createValue(String id) {
		return valueMap.computeIfAbsent(id, i->new SimpleGimValue());
	}

	@Override
	public String getPath() {
		return "";
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public int getIndex() {
		return -1;
	}

	@Override
	public Gim getChild(String key) {
		return rootRef.getChild(key);
	}

	@Nullable
	@Override
	public Gim getParent() {
		return null;
	}

	@Override
	public Set<String> getChildren() {
		return rootRef.getChildren();
	}

	@Override
	public boolean hasValue() {
		return rootRef.hasValue();
	}

	@Nullable
	@Override
	public String asString(@Nullable String def) {
		return rootRef.asString(def);
	}

	@Nullable
	@Override
	public Number asNumber(@Nullable Number def) {
		return rootRef.asNumber(def);
	}

	@Override
	public void remove() {
		rootRef.remove();
	}

	@Override
	public void setString(@Nullable String val) {
		rootRef.setString(val);
	}

	@Override
	public void setNumber(@Nullable Number val) {
		rootRef.setNumber(val);
	}

	private static class Children {
		private final String id;
		private final @Nullable Children parentChildren;
		private final Set<String> children;
		private final Set<String> pubChildren;

		private Children(String id, @Nullable Children parentChildren) {
			this.id = id;
			this.parentChildren = parentChildren;
			this.children = new HashSet<>();
			this.pubChildren = Collections.unmodifiableSet(this.children);
		}
	}

	private class GimRef implements Gim {
		private final String path;
		private final String name;
		private final int index;
		private final @Nullable Gim parent;

		private GimRef(String path, String name, int index, @Nullable GimRef parent) {
			this.path = path;
			this.name = name;
			this.index = index;
			this.parent = parent;
		}

		private GimRef(String path) {
			int dot = path.indexOf('.');
			this.path = path;
			this.name = path.substring(dot + 1);
			this.index = MathUtils.parsePositiveInt(this.name);
			this.parent = path.length() == 0 ? null :
					dot < 0 ? SimpleGim.this : references.computeIfAbsent(path.substring(0, dot), GimRef::new);
		}

		@Override
		public String getPath() {
			return path;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int getIndex() {
			return index;
		}

		@Override
		public Gim getChild(String key) {
			return SimpleGim.this.getChild(path.length() == 0 ? key : path + "." + key);
		}

		@Nullable
		@Override
		public Gim getParent() {
			return parent;
		}

		@Override
		public Set<String> getChildren() {
			return createChildren(path).pubChildren;
		}

		@Override
		public boolean hasValue() {
			SimpleGimValue val = valueMap.get(path);
			return val != null && val.hasValue();
		}

		@Nullable
		@Override
		public String asString(@Nullable String def) {
			@Nullable SimpleGimValue val = valueMap.get(path);
			return val == null ? def : val.asString(def);
		}

		@Nullable
		@Override
		public Number asNumber(@Nullable Number def) {
			@Nullable SimpleGimValue val = valueMap.get(path);
			return val == null ? def : val.asNumber(def);
		}

		@Override
		public void remove() {
			SimpleGim.this.remove(path);
		}

		@Override
		public void setString(@Nullable String val) {
			if (val == null) {
				remove();
				return;
			}
			SimpleGim.this.createChildren(path);
			SimpleGim.this.createValue(path).setString(val);
		}

		@Override
		public void setNumber(@Nullable Number val) {
			if (val == null) {
				remove();
				return;
			}
			SimpleGim.this.createChildren(path);
			SimpleGim.this.createValue(path).setNumber(val);
		}
	}
}
