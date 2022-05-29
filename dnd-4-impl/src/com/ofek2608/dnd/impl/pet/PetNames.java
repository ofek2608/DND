package com.ofek2608.dnd.impl.pet;

import com.ofek2608.utils.ResourcesUtils;

import java.util.Random;

public final class PetNames {
	private PetNames() {}
	public static void loadClass() {}

	private static final String[] NAMES = ResourcesUtils.getString("pet_names.txt").split("\n");

	public static String get(Random r) {
		return NAMES[r.nextInt(NAMES.length)];
	}

}
