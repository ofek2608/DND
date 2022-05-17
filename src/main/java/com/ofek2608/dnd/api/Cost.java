package com.ofek2608.dnd.api;

import com.ofek2608.dnd.api.player.Inventory;

public interface Cost {
	Cost EMPTY = (player, remove) -> true;
	Cost CANT = (player, remove) -> false;

	/**
	 * @param inventory the inventory
	 * @param remove if it should remove the cost, or just check if the inventory have enough items/money/whatever
	 * @return true if the inventory has/had enough items/money/whatever
	 */
	boolean check(Inventory inventory, boolean remove);
}
