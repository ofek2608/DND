package com.ofek2608.gim_old;

import java.util.HashSet;
import java.util.Set;

public final class GimTools {
	private GimTools() {}

	public static Gim copy(Gim gim) {
		Gim newGim = new SimpleGim();
		addAll(newGim, gim);
		return newGim;
	}

	public static void addAll(Gim dst, Gim src) {
		for (String key : src.keySet())
			dst.getChild(key).setString(src.getChild(key).asString());
	}

	public static void addAllSafe(Gim dst, Gim src) {
		for (String key : src.keySet()) {
			Gim dstChild = dst.getChild(key);
			if (!dstChild.hasValue())
				dstChild.setString(src.getChild(key).asString());
		}
	}








	static Set<String> keySet(Gim gim) {
		Set<String> res = new HashSet<>();
		keySetRec(gim, new StringBuilder(), res);
		return res;
	}

	private static void keySetRec(Gim gim, StringBuilder builder, Set<String> res) {
		if (gim.hasValue())
			res.add(builder.toString());

		if (builder.length() > 0)
			builder.append('.');
		int l = builder.length();
		for (String child : gim.getChildren()) {
			builder.append(child);
			keySetRec(gim.getChild(child), builder, res);
			builder.setLength(l);
		}
	}
}
