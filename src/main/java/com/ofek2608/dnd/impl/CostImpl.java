package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.Player;

public final class CostImpl implements Cost {
	private final long money;
	private final ItemList items;
	private final ItemList tickets;

	public CostImpl(long money, ItemList items, ItemList tickets) {
		this.money = money;
		this.items = items;
		this.tickets = tickets;
	}

	@Override
	public boolean check(Player player, boolean remove) {
		//Validate
		if (player.getMoney() < money || !items.has(player) || !tickets.has(player))
			return false;

		//remove
		if (remove) {
			player.addMoney(-money);
			items.remove(player);
		}

		return true;
	}
}
