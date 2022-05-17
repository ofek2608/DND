package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.PlayerStats;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlayerStatsImpl implements PlayerStats {
	private long lastIncome;

	@Override
	public Object saveJson() {
		return Map.of(
				"lastIncome", lastIncome
		);
	}

	@Override
	public void loadJson(@Nullable Object json) {
		if (!(json instanceof Map<?,?> jsonMap))
			return;
		lastIncome = jsonMap.get("lastIncome") instanceof Number n ? n.longValue() : 0;
	}

	@Override
	public void setLastIncome(long lastIncome) {
		this.lastIncome = lastIncome;
	}

	@Override
	public long getLastIncome() {
		return lastIncome;
	}
}
