package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;

import javax.annotation.Nullable;

public interface PlayerEquipmentSlot extends Savable {
	/**
	 * @return the item, or null if the item is set to null, or player don't have the item
	 */
	@Nullable String getItem();

	/**
	 * @param item the item in the slot
	 */
	void setItem(@Nullable String item);
}
