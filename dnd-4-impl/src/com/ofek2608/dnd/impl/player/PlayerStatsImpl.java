package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.PlayerStats;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;

public class PlayerStatsImpl implements PlayerStats {
	private long lastDeath;
	private long lastIncome;
	private float lastDamage;

	@Override
	public void save(GimBuilder builder) {
		builder.child("lastDeath" ).set(lastDeath);
		builder.child("lastIncome").set(lastIncome);
		builder.child("lastDamage").set(lastDamage);
	}

	@Override
	public void load(Gim data) {
		lastDeath  = data.get("lastDeath" ).getLong ();
		lastIncome = data.get("lastIncome").getLong ();
		lastDamage = data.get("lastDamage").getFloat();
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
