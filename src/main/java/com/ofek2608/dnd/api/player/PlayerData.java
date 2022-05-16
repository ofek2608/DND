package com.ofek2608.dnd.api.player;

import javax.annotation.Nullable;

public interface PlayerData {
	Inventory getInventory();
	Inventory getBackpack();
	PlayerHealth getHealth();

	void setRegion(@Nullable String region);
	@Nullable String getRegion();
}
