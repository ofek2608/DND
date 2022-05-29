package com.ofek2608.dnd.data.enemy;

import com.ofek2608.dnd.resources.Res;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.utils.Weights;

public final class EnemyType extends LoadableGameData {
	@Res public final int maxHealth = fieldInt();
	@Res public final EnemyAttack[] attacks = field(new EnemyAttack[0]);
	public final Weights<EnemyAttack> attackWeights = Weights.createFromFunc(attacks, attack->attack.weight);

	public EnemyType(LoadContext ctx) {
		super(ctx);
	}
}
