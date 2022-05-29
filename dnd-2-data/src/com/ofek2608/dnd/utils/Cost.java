package com.ofek2608.dnd.utils;

import com.ofek2608.dnd.resources.LoadContext;

public final class Cost {
	public static final Cost EMPTY = new Cost(0, ItemList.EMPTY);


	public final long money;
	public final ItemList items;

	public Cost(long money, ItemList items) {
		this.money = money;
		this.items = items;
	}

	public Cost(LoadContext ctx) {
		this.money = ctx.data.get("money").getLong();
		this.items = new ItemList(ctx.get("items"));
	}
}
