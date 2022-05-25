package com.ofek2608.dnd.impl.enemy;

import com.ofek2608.dnd.api.enemy.EnemyAttack;
import com.ofek2608.dnd.api.enemy.EnemyType;
import com.ofek2608.dnd.utils.Weight;

import java.util.Random;

public class EnemyTypeImpl implements EnemyType {
	private final String id;
	private final int maxHealth;
	private final Weight<EnemyAttack> attacks;

	public EnemyTypeImpl(String id, int maxHealth, Weight<EnemyAttack> attacks) {
		this.id = id;
		this.maxHealth = maxHealth;
		this.attacks = attacks;
	}


	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getMaxHealth() {
		return maxHealth;
	}

	@Override
	public EnemyAttack getAttack(Random r) {
		return attacks.get(r);
	}
}
