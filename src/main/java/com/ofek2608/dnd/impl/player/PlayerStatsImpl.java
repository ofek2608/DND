package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.PlayerStats;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlayerStatsImpl implements PlayerStats {
	private long lastDeath;
	private long lastIncome;
	private float lastDamage;

	@Override
	public Object saveJson() {
		return Map.of(
				"lastDeath" , lastDeath ,
				"lastIncome", lastIncome,
				"lastDamage", lastDamage
		);
	}

	@Override
	public void loadJson(@Nullable Object json) {
		if (!(json instanceof Map<?,?> jsonMap))
			return;
		lastDeath  = jsonMap.get("lastDeath" ) instanceof Number n ? n.longValue()  : 0;
		lastIncome = jsonMap.get("lastIncome") instanceof Number n ? n.longValue()  : 0;
		lastDamage = jsonMap.get("lastDamage") instanceof Number n ? n.floatValue() : 0;
	}

	@Override
	public void setLastDeath(long lastDeath) {
		this.lastDeath = lastDeath;
	}

	@Override
	public long getLastDeath() {
		return lastDeath;
	}

	@Override
	public void setLastIncome(long lastIncome) {
		this.lastIncome = lastIncome;
	}

	@Override
	public long getLastIncome() {
		return lastIncome;
	}

	@Override
	public void setLastDamage(float lastDamage) {
		this.lastDamage = lastDamage;
	}

	@Override
	public float getLastDamage() {
		return lastDamage;
	}
}
