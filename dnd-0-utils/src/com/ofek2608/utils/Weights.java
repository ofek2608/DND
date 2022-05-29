package com.ofek2608.utils;

import com.ofek2608.functions.ToFloatFunction;

import java.util.Collection;
import java.util.Random;
import java.util.function.Function;

public class Weights<T> {
	private final T[] items;
	private final float[] totals;

	public Weights(T[] items, float[] weights) {
		int length = items.length;
		if (length == 0 || weights.length != length)
			throw new IllegalArgumentException("Illegal array lengths");

		this.items = items;
		int total = 0;
		float[] totals = this.totals = new float[length];
		for (int i = 0; i < length; i++) {
			float weight = weights[i];
			if (weight <= 0)
				throw new IllegalArgumentException("Weight is not positive (" + weight + ")");
			totals[i] = total += weight;
		}
	}

	public int getItemCount() {
		return items.length;
	}

	public T getItem(int index) {
		return items[index];
	}

	public int getIndex(float rnd) {
		float[] totals = this.totals;

		int min = 0;
		int max = totals.length - 1;
		rnd *= totals[max];

		while (min < max) {
			int predicate = (min + max) >> 1;
			if (rnd < totals[predicate])
				max = predicate;
			else
				min = predicate + 1;
		}
		return min;
	}

	public T get(float rnd) {
		return getItem(getIndex(rnd));
	}

	public T get(Random r) {
		return get(r.nextFloat());
	}


	@SuppressWarnings("unchecked")
	public static <S,T> Weights<T> createFromPairs(S[] pairs, Function<? super S, ? extends T> itemFunc, ToFloatFunction<? super S> weightFunc) {
		int length = pairs.length;

		T[] items = (T[])new Object[length];
		float[] weights = new float[length];

		for (int i = 0; i < length; i++) {
			S pair = pairs[i];
			items[i] = itemFunc.apply(pair);
			weights[i] = weightFunc.applyAsFloat(pair);
		}

		return new Weights<>(items, weights);
	}

	@SuppressWarnings("unchecked")
	public static <S,T> Weights<T> createFromPairs(Collection<? extends S> pairs, Function<? super S, ? extends T> itemFunc, ToFloatFunction<? super S> weightFunc) {
		return createFromPairs((S[])pairs.toArray(), itemFunc, weightFunc);
	}

	public static <T> Weights<T> createFromFunc(T[] collection, ToFloatFunction<? super T> func) {
		return createFromPairs(collection, t->t, func);
	}

	public static <T> Weights<T> createFromFunc(Collection<? extends T> collection, ToFloatFunction<? super T> func) {
		return createFromPairs(collection, t->t, func);
	}
}
