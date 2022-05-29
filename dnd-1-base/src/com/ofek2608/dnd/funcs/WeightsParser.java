package com.ofek2608.dnd.funcs;

import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.gim.Gim;
import com.ofek2608.utils.Weights;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public final class WeightsParser {
	private WeightsParser() {}

	public static @Nullable Weights<String> parseNullableStringWeights(LoadContext ctx) {
		return parseNullableWeights(ctx, a-> Objects.requireNonNull(a.data.valueString));
	}

	public static Weights<String> parseStringWeights(LoadContext ctx) {
		return parseWeights(ctx, a-> Objects.requireNonNull(a.data.valueString));
	}

	public static <T> @Nullable Weights<T> parseNullableWeights(LoadContext ctx, Function<LoadContext,T> constructor) {
		try {
			return parseWeights(ctx, constructor);
		} catch (RuntimeException ignored) {}
		return null;
	}

	@Contract("_, _ -> new")
	public static <T> Weights<T> parseWeights(LoadContext ctx, Function<LoadContext,T> constructor) {
		Gim[] children = ctx.data.getChildren();
		int childrenCount = children.length;

		@SuppressWarnings("unchecked")
		T[] items = (T[]) new Object[childrenCount];
		float[] weights = new float[childrenCount];


		int itemIndex = 0;
		for (int i = 0; i < childrenCount; i++) {
			float weight = children[i].getFloat();
			if (weight > 0 && i < childrenCount - 1) {
				weights[itemIndex] = weight;
				i++;
			} else {
				weights[itemIndex] = 1;
			}
			items[itemIndex] = constructor.apply(ctx.of(children[i]));
			itemIndex++;
		}


		return new Weights<>(Arrays.copyOf(items, itemIndex), Arrays.copyOf(weights, itemIndex));
	}
}
