package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.equipment.PlayerEquipmentSlot;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;
import org.jetbrains.annotations.Nullable;

public class PlayerEquipmentSlotImpl implements PlayerEquipmentSlot {
	private final Inventory inventory;
	@Nullable private String item;

	public PlayerEquipmentSlotImpl(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public void save(GimBuilder builder) {
		builder.set(item);
	}

	@Override
	public void load(Gim data) {
		item = data.valueString;
	}

	@Nullable
	@Override
	public String getItem() {
		return item == null || inventory.getItem(item) == 0 ? null : item;
	}

	@Override
	public void setItem(@Nullable String item) {
		this.item = item;
	}
}
