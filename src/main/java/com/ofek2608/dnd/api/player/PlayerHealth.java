package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;

public interface PlayerHealth extends Savable {
	int REVIVE_TIME = 5000;//21600000
	float getHealth();
	void setHealth(float health);
	void damage(float value);
	void kill();
	long getLastDeath();

	default long getReviveTime() {
		return getLastDeath() + REVIVE_TIME;
	}

	default boolean isAlive() {
		return getReviveTime() <= System.currentTimeMillis();
	}
}
