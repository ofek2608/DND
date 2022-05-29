package com.ofek2608.dnd.data.lang;

import com.ofek2608.dnd.funcs.WeightsParser;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.gim.Gim;
import com.ofek2608.utils.Weights;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public final class Lang extends LoadableGameData {
	private final Map<String,Object> map = new HashMap<>();

	public Lang(LoadContext ctx) {
		super(ctx);
		int cutIndex = ctx.data.path.length() + 1;
		for (Gim grandChild : ctx.data.getGrandChildren()) {
			String key = grandChild == ctx.data ? "" : grandChild.path.substring(cutIndex);

			@Nullable
			Object value = grandChild.valueString;
			if (value == null)
				value = WeightsParser.parseNullableStringWeights(ctx.of(grandChild));

			if (value != null)
				map.put(key, value);
		}
	}

	public String get(String key, @Nullable Random r) {
		Object val = map.get(key);

		if (val instanceof Weights<?> w)
			val = w.get(r == null ? 0 : r.nextFloat());

		return val instanceof String s ? s : key;
	}

	public String get(String key) {
		return get(key, null);
	}
}
