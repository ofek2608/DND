package com.ofek2608.dnd.api;

import org.jetbrains.annotations.Nullable;

public enum EquipmentSlot {
	HELMET,CHESTPLATE,LEGGINGS,BOOTS,SWORD,WAND,SHIELD,FOOD;

	@Nullable
	public static EquipmentSlot parse(String str) {
		return switch (str.toUpperCase()) {
			case "HELMET"     -> HELMET;
			case "CHESTPLATE" -> CHESTPLATE;
			case "LEGGINGS"   -> LEGGINGS;
			case "BOOTS"      -> BOOTS;
			case "SWORD"      -> SWORD;
			case "WAND"       -> WAND;
			case "SHIELD"     -> SHIELD;
			case "FOOD"       -> FOOD;
			default -> null;
		};
	}
}
