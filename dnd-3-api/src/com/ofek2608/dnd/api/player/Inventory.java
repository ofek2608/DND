package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.data.item.Item;
import com.ofek2608.dnd.utils.Cost;
import com.ofek2608.dnd.utils.ItemList;

import java.util.Set;

public interface Inventory extends Savable {
	long getMoney();
	void setMoney(long money);
	int getItem(String item);
	void setItem(String item, int count);
	void clear();
	void addAll(Inventory other);
	Set<String> getItems();

	default void addMoney(long money) {
		setMoney(getMoney() + money);
	}

	default void addItem(String item, int count) {
		setItem(item, getItem(item) + count);
	}

	default int getItem(Item item) {
		return getItem(item.getPath());
	}

	default void setItem(Item item, int count) {
		setItem(item.getPath(), count);
	}

	default void addItem(Item item, int count) {
		addItem(item.getPath(), count);
	}


	default boolean hasItemsTimes(ItemList lst, int times) {
		String[] items = lst.items;
		int[] counts = lst.counts;
		int length = items.length;

		for (int i = 0; i < length; i++)
			if (getItem(items[i]) < times * counts[i])
				return false;
		return true;
	}

	default boolean hasItems(ItemList lst) {
		return hasItemsTimes(lst, 1);
	}


	default void addItemsTimes(ItemList lst, int times) {
		String[] items = lst.items;
		int[] counts = lst.counts;
		int length = items.length;

		for (int i = 0; i < length; i++)
			addItem(items[i], times * counts[i]);
	}

	default void addItems(ItemList lst) {
		addItemsTimes(lst, 1);
	}

	default void removeItemsTimes(ItemList lst, int times) {
		addItemsTimes(lst, -times);
	}

	default void removeItems(ItemList lst) {
		addItemsTimes(lst, -1);
	}


	default boolean checkCost(Cost cost, boolean remove) {
		//Validate
		if (getMoney() < cost.money || !hasItems(cost.items))
			return false;

		//remove
		if (remove) {
			addMoney(-cost.money);
			removeItems(cost.items);
		}

		return true;
	}
}
