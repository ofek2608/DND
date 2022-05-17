package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;

import javax.annotation.Nullable;

public interface PlayerData extends Savable {
	Inventory getInventory();
	Inventory getBackpack();
	Inventory getBothInventories();
	PlayerHealth getHealth();

	void setRegion(@Nullable String region);
	@Nullable String getRegion();
}
