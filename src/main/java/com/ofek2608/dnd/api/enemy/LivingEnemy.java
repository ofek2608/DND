package com.ofek2608.dnd.api.enemy;

public interface LivingEnemy {
	/**
	 * @return the type of the enemy
	 */
	EnemyType getType();

	/**
	 * Changes the health of the enemy
	 * If health is outside the range [0, getType().getMaxHealth()], its capped
	 * @param value the new value of the health
	 */
	void setHealth(int value);

	/**
	 * removes health from the enemy
	 * @param damage the amount of health to remove
	 */
	void damage(int damage);

	/**
	 * @return the health (must be 0 or positive)
	 */
	int getHealth();

	/**
	 * @return if health==0
	 */
	boolean isDead();
}
