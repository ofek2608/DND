package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.EquipmentSlot;
import com.ofek2608.dnd.api.Savable;

public interface PlayerEquipments extends Savable {
	PlayerEquipmentSlot getHelmet();
	PlayerEquipmentSlot getChestplate();
	PlayerEquipmentSlot getLeggings();
	PlayerEquipmentSlot getBoots();
	PlayerEquipmentSlot getSword();
	PlayerEquipmentSlot getWand();
	PlayerEquipmentSlot getShield();
	PlayerEquipmentSlot getFood();

	default PlayerEquipmentSlot get(EquipmentSlot slot) {
		return switch (slot) {
			case HELMET     -> getHelmet();
			case CHESTPLATE -> getChestplate();
			case LEGGINGS   -> getLeggings();
			case BOOTS      -> getBoots();
			case SWORD      -> getSword();
			case WAND       -> getWand();
			case SHIELD     -> getShield();
			case FOOD       -> getFood();
		};
	}
}
