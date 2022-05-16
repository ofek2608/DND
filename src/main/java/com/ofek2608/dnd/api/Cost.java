package com.ofek2608.dnd.api;

public interface Cost {
	Cost EMPTY = (player, remove) -> true;

	/**
	 * @param player the player
	 * @param remove if it should remove the cost, or just check if the player have enough items/money/whatever
	 * @return true if the player has/had enough items/money/whatever
	 */
	boolean check(Player player, boolean remove);
}
