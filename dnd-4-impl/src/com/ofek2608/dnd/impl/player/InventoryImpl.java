package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InventoryImpl implements Inventory {
	private long money = 0;
	private final Map<String, Integer> items = new HashMap<>();
	private final Set<String> unmodifiableItemsSet = Collections.unmodifiableSet(items.keySet());

	@Override
	public void save(GimBuilder builder) {
		builder.child("money").set(money);
		GimBuilder builderItems = builder.child("items");
		items.forEach((item,count)->builderItems.child(item).set(count));
	}

	@Override
	public void load(Gim data) {
		money = data.get("money").getLong();

		items.clear();
		for (Gim item : data.get("items")) {
			int count = item.getInt();
			if (count > 0)
				items.put(item.name, count);
		}
	}

	@Override
	public long getMoney() {
		return money;
	}

	@Override
	public void setMoney(long money) {
		this.money = Math.max(0, money);
	}

	@Override
	public int getItem(String item) {
		return items.getOrDefault(item, 0);
	}

	@Override
	public void setItem(String item, int count) {
		if (count <= 0)
			items.remove(item);
		else
			items.put(item, count);
	}

	@Override
	public void clear() {
		money = 0;
		items.clear();
	}

	@Override
	public void addAll(Inventory other) {
		money += other.getMoney();
		//no problems with current modifications
		other.getItems().forEach(item->addItem(item, other.getItem(item)));
	}

	@Override
	public Set<String> getItems() {
		return unmodifiableItemsSet;
	}
}
