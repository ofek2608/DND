package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.PlayerData;
import com.ofek2608.dnd.api.player.PlayerHealth;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class PlayerHealthImpl implements PlayerHealth {
	private final PlayerData player;
	private float health;
	private long lastDeath;

	public PlayerHealthImpl(PlayerData player) {
		this.player = player;
	}

	@Override
	public Object saveJson() {
		return Map.of(
				"health", health,
				"lastDeath", lastDeath
		);
	}

	@Override
	public void loadJson(@Nullable Object json) {
		Map<?,?> jsonMap = json instanceof Map<?,?> m ? m : Collections.EMPTY_MAP;
		health    = jsonMap.get("heath"    ) instanceof Number n ? n.floatValue() : 1;
		lastDeath = jsonMap.get("lastDeath") instanceof Number n ? n.longValue() : 0;
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public void setHealth(float health) {
		this.health = health;
	}

	@Override
	public void damage(float value) {
		float newHealth = this.health - value;
		if (newHealth >= 0)
			health = newHealth;
		else
			kill();
	}

	@Override
	public void kill() {
		lastDeath = System.currentTimeMillis();
		health = 1;
		player.setRegion(null);
		player.getBackpack().clear();
	}

	@Override
	public long getLastDeath() {
		return lastDeath;
	}
}
