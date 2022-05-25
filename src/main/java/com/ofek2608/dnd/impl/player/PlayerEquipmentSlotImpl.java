package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.PlayerEquipmentSlot;
import org.jetbrains.annotations.Nullable;

public class PlayerEquipmentSlotImpl implements PlayerEquipmentSlot {
	private final Inventory inventory;
	@Nullable private String item;

	public PlayerEquipmentSlotImpl(Inventory inventory) {
		this.inventory = inventory;
	}


	@Override
	public Object saveJson() {
		return item == null ? "" : item;
	}

	@Override
	public void loadJson(@Nullable Object json) {
		item = json instanceof String s && s.length() > 0 ? s : null;
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
