package com.ofek2608.dnd.data.item;

import com.ofek2608.dnd.utils.Roll;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;
import org.jetbrains.annotations.Nullable;

public final class Item extends LoadableGameData {
	@Res public final String category = field("uncategorized");
	@Res public final String icon = field(":cookie:");
	@Res public final @Nullable EquipmentSlot slot = field();
	@Res public final @Nullable Roll damage = Roll.parseCtx(fieldCtx());
	@Res public final float protection = fieldFloat();
	@Res public final float heal = fieldFloat();

	public Item(LoadContext ctx) {
		super(ctx);
	}
}
