package com.ofek2608.dnd.api.enemy;

import com.ofek2608.dnd.api.Identifiable;

import java.util.Random;

public interface EnemyType extends Identifiable {
	/**
	 * @return the maximum amount of health of the enemy
	 */
	int getMaxHealth();

	/**
	 * @param r the random number generator
	 * @return a random attack the enemy can do
	 */
	EnemyAttack getAttack(Random r);
}
