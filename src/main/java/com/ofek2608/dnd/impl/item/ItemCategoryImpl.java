package com.ofek2608.dnd.impl.item;

import com.ofek2608.dnd.api.item.ItemCategory;

public class ItemCategoryImpl implements ItemCategory {
	private final String id;
	private final String name;
	private final String icon;

	public ItemCategoryImpl(String name, String icon) {
		this.id = "item_category." + name;
		this.name = name;
		this.icon = icon;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIcon() {
		return icon;
	}
}
