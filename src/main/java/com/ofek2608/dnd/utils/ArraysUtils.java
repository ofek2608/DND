package com.ofek2608.dnd.utils;

import java.util.List;

public final class ArraysUtils {
	private ArraysUtils() {}

	public static float[] toArray(List<Float> list) {
		int s = list.size();
		float[] arr = new float[s];

		for (int i = 0; i < s; i++)
			arr[i] = list.get(i);

		return arr;
	}
}
