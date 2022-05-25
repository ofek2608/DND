package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerHealth;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class PlayerHealthImpl implements PlayerHealth {
	private final Player player;
	private float health;
	private long lastDeath;

	public PlayerHealthImpl(Player player) {
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
		//value *= 1 - protection;
		float newHealth = this.health - value;
		if (newHealth < 0) {
			kill();
			return;
		}
		health = newHealth;
		player.getStats().setLastDamage(value);
	}

	@Override
	public void kill() {
		lastDeath = System.currentTimeMillis();
		health = 1;
		player.getData().setRegion(null);
		player.getData().getBackpack().clear();
	}

	@Override
	public long getLastDeath() {
		return lastDeath;
	}
}
