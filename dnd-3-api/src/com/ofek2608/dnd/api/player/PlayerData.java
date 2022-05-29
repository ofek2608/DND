package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.api.player.equipment.PlayerEquipments;
import com.ofek2608.dnd.api.player.pet.PlayerPets;

import javax.annotation.Nullable;

public interface PlayerData extends Savable {
	Inventory getInventory();
	Inventory getBackpack();
	Inventory getBothInventories();
	PlayerEquipments getEquipment();
	PlayerPets getPets();

	void setRegion(@Nullable String region);
	@Nullable String getRegion();

	int REVIVE_TIME = 5000;//21600000
	void setHealth(float health);
	float getHealth();
	void damage(float value);
	void kill();
	long getReviveTime();
	default boolean isAlive() {
		return getReviveTime() <= System.currentTimeMillis();
	}
}
