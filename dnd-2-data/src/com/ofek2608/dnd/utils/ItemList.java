package com.ofek2608.dnd.utils;

import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.gim.Gim;

import java.util.Arrays;

public final class ItemList {
	public static final ItemList EMPTY = new ItemList(new String[0], new int[0]);

	public final String[] items;
	public final int[] counts;

	public ItemList(String[] items, int[] counts) {
		this.items = items;
		this.counts = counts;
	}

	public ItemList(LoadContext ctx) {
		Gim[] dataChildren = ctx.data.getChildren();
		int len = dataChildren.length;

		int[] itemCounts = new int[len];
		String[] itemNames = new String[len];

		int itemIndex = 0;
		for (int i = 0; i < len; i++) {
			int itemCount = dataChildren[i].getInt();
			if (itemCount > 0 && i < len - 1) {
				itemCounts[itemIndex] = itemCount;
				i++;
			} else {
				itemCounts[itemIndex] = 1;
			}
			itemNames[itemIndex] = "item." + dataChildren[i].getString("");
			itemIndex++;
		}

		this.items = Arrays.copyOf(itemNames, itemIndex);
		this.counts = Arrays.copyOf(itemCounts, itemIndex);
	}
}
