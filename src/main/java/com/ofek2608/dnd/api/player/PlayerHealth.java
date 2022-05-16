package com.ofek2608.dnd.api.player;

public interface PlayerHealth {
	int REVIVE_TIME = 5000;//21600000
	float getHealth();
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
