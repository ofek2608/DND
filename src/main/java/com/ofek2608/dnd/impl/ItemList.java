package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.api.player.Inventory;

import javax.annotation.Nullable;
import java.util.List;

public final class ItemList {
	public static final ItemList EMPTY = new ItemList(new String[0], new int[0]);

	private final int length;
	private final String[] names;
	private final int[] counts;

	public ItemList(String[] names, int[] counts) {
		this.length = names.length;
		this.names = names;
		this.counts = counts;
	}

	public boolean has(Inventory inventory) {
		return hasTimes(inventory, 1);
	}

	public boolean hasTimes(Inventory inventory, int times) {
		int length = this.length;
		String[] names = this.names;
		int[] counts = this.counts;

		for (int i = 0; i < length; i++)
			if (inventory.getItem(names[i]) < times * counts[i])
				return false;
		return true;
	}

	public void addTimes(Inventory inventory, int times) {
		int length = this.length;
		String[] names = this.names;
		int[] counts = this.counts;

		for (int i = 0; i < length; i++)
			inventory.addItem(names[i], times * counts[i]);
	}

	public void add(Inventory inventory) {
		addTimes(inventory, 1);
	}

	public void removeTimes(Inventory inventory, int times) {
		addTimes(inventory, -times);
	}

	public void remove(Inventory inventory) {
		addTimes(inventory, -1);
	}





	@Nullable
	public static ItemList parseItemList(Object json) {
		if (!(json instanceof List<?> list))
			return null;


		int size = list.size();
		if ((size & 1) == 1)
			return null;

		int halfSize = size >> 1;

		int[] counts = new int[halfSize];
		String[] items = new String[halfSize];

		for (int i = 0; i < halfSize; i++) {
			Object count = list.get(i << 1);
			Object item = list.get(i << 1 | 1);

			if (!(count instanceof Number) || !(item instanceof String))
				return null;

			counts[i] = ((Number)count).intValue();
			items[i] = "item." + item;
		}

		return new ItemList(items, counts);
	}
}
