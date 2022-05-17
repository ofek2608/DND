package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class BothInventories implements Inventory {
	private final Inventory inv0;
	private final Inventory inv1;

	public BothInventories(Inventory inv0, Inventory inv1) {
		this.inv0 = inv0;
		this.inv1 = inv1;
	}

	@Override public Object saveJson() {return new Object();}
	@Override public void loadJson(@Nullable Object json) {}

	@Override
	public long getMoney() {
		return inv0.getMoney() + inv1.getMoney();
	}

	@Override
	public void setMoney(long money) {
		long m0 = inv0.getMoney();
		if (money >= m0)
			inv1.setMoney(money - m0);
		else {
			inv1.setMoney(0);
			inv0.setMoney(money);
		}
	}

	@Override
	public int getItem(String item) {
		return inv0.getItem(item) + inv1.getItem(item);
	}

	@Override
	public void setItem(String item, int count) {
		int c0 = inv0.getItem(item);
		if (count >= c0)
			inv1.setItem(item, count - c0);
		else {
			inv1.setItem(item, 0);
			inv0.setItem(item, count);
		}
	}

	@Override
	public void clear() {
		inv0.clear();
		inv1.clear();
	}

	@Override
	public void addAll(Inventory other) {
		addMoney(other.getMoney());
		other.getItems().forEach(item->addItem(item, other.getItem(item)));
	}

	@Override
	public Set<String> getItems() {
		Set<String> items = new HashSet<>(inv0.getItems());
		items.addAll(inv1.getItems());
		return items;
	}
}
