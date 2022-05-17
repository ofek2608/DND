package com.ofek2608.dnd.impl.item;

import com.ofek2608.dnd.api.item.Item;
import com.ofek2608.dnd.api.Roll;
import org.jetbrains.annotations.Nullable;

public class ItemImpl implements Item {
	private final String id;
	private final String name;
	private final String category;
	private final String icon;
	private final @Nullable Roll attack;
	private final float protection;
	private final float heal;

	public ItemImpl(String name, String category, String icon, @Nullable Roll attack, float protection, float heal) {
		this.id = "item." + name;
		this.name = name;
		this.category = category;
		this.icon = icon;
		this.attack = attack;
		this.protection = protection;
		this.heal = heal;
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
	public String getCategory() {
		return category;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	@Nullable
	@Override
	public Roll getAttack() {
		return attack;
	}

	@Override
	public float getProtection() {
		return protection;
	}

	@Override
	public float getHeal() {
		return heal;
	}
}
