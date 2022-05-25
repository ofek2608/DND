package com.ofek2608.dnd.api.enemy;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.Roll;

import javax.annotation.Nullable;

public interface EnemyAttack extends Identifiable {
	/**
	 * @return the attack's damage
	 */
	@Nullable Roll getDamage();

	/**
	 * @return the attack's healing
	 */
	@Nullable Roll getHeal();

	/**
	 * @return the attack's kind
	 */
	@Nullable AttackKind getKind();
}
