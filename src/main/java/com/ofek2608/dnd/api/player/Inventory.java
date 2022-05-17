package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.api.item.Item;

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
		return getItem(item.getId());
	}

	default void setItem(Item item, int count) {
		setItem(item.getId(), count);
	}

	default void addItem(Item item, int count) {
		addItem(item.getId(), count);
	}
}
