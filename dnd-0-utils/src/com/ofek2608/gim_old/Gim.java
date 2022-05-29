package com.ofek2608.gim_old;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * General Identifier Map
 */
public interface Gim extends GimValue {
	String getPath();
	String getName();
	int getIndex();

	Gim getChild(String key);
	default Gim getChild(int index) {
		if (index < 0)
			throw new IndexOutOfBoundsException(index);
		return getChild("" + index);
	}

	@Nullable Gim getParent();

	default @Nullable Gim getParent(int steps) {
		if (steps < 0)
			throw new IllegalArgumentException("steps must be non negative (" + steps + ")");
		@Nullable
		Gim gim = this;
		while (steps-- > 0 && gim != null)
			gim = gim.getParent();
		return gim;
	}

	Set<String> getChildren();

	default boolean isArray() {
		Set<String> children = getChildren();
		int s = children.size();
		for (int i = 0; i < s; i++)
			if (!children.contains("" + i))
				return false;
		return true;
	}

	default @Nullable Gim offsetChild(int off) {
		@Nullable Gim parent = getParent();
		int index = getIndex();
		int newIndex = index + off;

		if (parent == null || index < 0 || newIndex < 0)
			return null;

		return parent.getChild(newIndex);
	}

	default @Nullable Gim nextChild() {
		return offsetChild(1);
	}

	default @Nullable Gim prevChild() {
		return offsetChild(-1);
	}



	default Set<String> keySet() {
		return GimTools.keySet(this);
	}



}
