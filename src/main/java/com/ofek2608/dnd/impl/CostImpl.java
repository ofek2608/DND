package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.player.Inventory;

public final class CostImpl implements Cost {
	private final long money;
	private final ItemList items;

	public CostImpl(long money, ItemList items) {
		this.money = money;
		this.items = items;
	}

	@Override
	public boolean check(Inventory inventory, boolean remove) {
		//Validate
		if (inventory.getMoney() < money || !items.has(inventory))
			return false;

		//remove
		if (remove) {
			inventory.addMoney(-money);
			items.remove(inventory);
		}

		return true;
	}
}
