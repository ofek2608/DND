package com.ofek2608.dnd.utils;

import java.util.Random;

public class Weight<T> {
	private final int l;
	private final T[] objects;
	private final float[] weights;
	private final float totalWeights;

	public Weight(T[] objects, float[] weights) {
		int l = objects.length;
		if (weights.length != l)
			throw new IllegalArgumentException();
		this.l = l;

		this.objects = objects;
		this.weights = weights;

		float totalWeights = 0;
		for (float weight : weights)
			totalWeights += weight;
		this.totalWeights = totalWeights;
	}

	public T get(Random r) {
		int l = this.l;
		if (l == 0)
			return null;

		T[] objects = this.objects;
		float[] weights = this.weights;

		float value = r.nextFloat() * totalWeights;
		for (int i = 0; i < l; i++) {
			value -= weights[i];
			if (value < 0)
				return objects[i];
		}
		return objects[l - 1];
	}

	public T get() {
		return l > 0 ? objects[0] : null;
	}
}
