package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;

public interface PlayerStats extends Savable {
	void setLastDeath(long lastDeath);
	long getLastDeath();
	void setLastIncome(long lastIncome);
	long getLastIncome();
	void setLastDamage(float lastDamage);
	float getLastDamage();
}
