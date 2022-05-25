package com.ofek2608.dnd.impl.enemy;

import com.ofek2608.dnd.api.Roll;
import com.ofek2608.dnd.api.enemy.AttackKind;
import com.ofek2608.dnd.api.enemy.EnemyAttack;
import org.jetbrains.annotations.Nullable;

public class EnemyAttackImpl implements EnemyAttack {
	private final String id;
	private final @Nullable Roll damage;
	private final @Nullable Roll heal;
	private final AttackKind kind;

	public EnemyAttackImpl(String id, @Nullable Roll damage, @Nullable Roll heal, AttackKind kind) {
		this.id = id;
		this.damage = damage;
		this.heal = heal;
		this.kind = kind;
	}

	@Override
	public String getId() {
		return id;
	}

	@Nullable
	@Override
	public Roll getDamage() {
		return damage;
	}

	@Nullable
	@Override
	public Roll getHeal() {
		return heal;
	}

	@Nullable
	@Override
	public AttackKind getKind() {
		return kind;
	}
}
