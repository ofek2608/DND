package com.ofek2608.dnd.data.enemy;

import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.dnd.utils.Roll;
import org.jetbrains.annotations.Nullable;

public final class EnemyAttack extends LoadableGameData {
	@Res public final float weight = fieldFloat();
	@Res public final @Nullable Roll damage = Roll.parseCtx(fieldCtx());
	@Res public final @Nullable Roll heal = Roll.parseCtx(fieldCtx());
	@Res public final AttackKind kind = field(AttackKind.GENERAL);

	public EnemyAttack(LoadContext ctx) {
		super(ctx);
	}
}
