package com.ofek2608.dnd.api.enemy;

import com.ofek2608.dnd.api.item.Item;
import com.ofek2608.dnd.api.player.PlayerEquipmentSlot;
import com.ofek2608.dnd.api.player.PlayerEquipments;
import com.ofek2608.dnd.resources.Resources;
import org.jetbrains.annotations.Nullable;

public enum AttackKind {
	HEAD {
		@Override
		float getProtection(PlayerEquipments equipments) {
			return getSlotProtection(equipments.getHelmet());
		}
	}, BODY {
		@Override
		float getProtection(PlayerEquipments equipments) {
			return getSlotProtection(equipments.getChestplate());
		}
	}, LEGS {
		@Override
		float getProtection(PlayerEquipments equipments) {
			return getSlotProtection(equipments.getLeggings());
		}
	}, FEET {
		@Override
		float getProtection(PlayerEquipments equipments) {
			return getSlotProtection(equipments.getBoots());
		}
	}, GENERAL {
		@Override
		float getProtection(PlayerEquipments equipments) {
			float val = 0;
			val += getSlotProtection(equipments.getHelmet());
			val += getSlotProtection(equipments.getChestplate());
			val += getSlotProtection(equipments.getLeggings());
			val += getSlotProtection(equipments.getBoots());
			return val / 4;
		}
	};

	abstract float getProtection(PlayerEquipments equipments);

	@Nullable
	public static AttackKind parse(String str) {
		return switch (str.toUpperCase()) {
			case "HEAD"    -> HEAD;
			case "BODY"    -> BODY;
			case "LEGS"    -> LEGS;
			case "FEET"    -> FEET;
			case "GENERAL" -> GENERAL;
			default -> null;
		};
	}

	private static float getSlotProtection(PlayerEquipmentSlot slot) {
		String itemId = slot.getItem();
		if (itemId == null)
			return 0;
		if (!(Resources.get(itemId) instanceof Item item))
			return 0;
		return item.getProtection();
	}
}
