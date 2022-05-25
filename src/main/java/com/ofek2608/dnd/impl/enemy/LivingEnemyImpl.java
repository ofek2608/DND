package com.ofek2608.dnd.impl.enemy;

import com.ofek2608.dnd.api.enemy.EnemyType;
import com.ofek2608.dnd.api.enemy.LivingEnemy;

public class LivingEnemyImpl implements LivingEnemy {
	private final EnemyType type;
	private int health;

	public LivingEnemyImpl(EnemyType type) {
		this.type = type;
		this.health = type.getMaxHealth();
	}

	public LivingEnemyImpl(EnemyType type, int health) {
		this.type = type;
		setHealth(health);
	}

	@Override
	public EnemyType getType() {
		return type;
	}

	@Override
	public void setHealth(int value) {
		health = Math.max(Math.min(value, type.getMaxHealth()), 0);
	}

	@Override
	public void damage(int damage) {
		setHealth(health - damage);
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public boolean isDead() {
		return health == 0;
	}
}
