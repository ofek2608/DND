package com.ofek2608.dnd.data.item;

import com.ofek2608.dnd.resources.Res;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;

import java.util.Map;

public final class Items extends LoadableGameData {
	@Res(mapKind = Item.class)
	public final Map<String, Item> items = fieldNonnull();

	public Items(LoadContext ctx) {
		super(ctx);
	}
}
