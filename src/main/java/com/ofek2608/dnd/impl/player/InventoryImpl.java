package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InventoryImpl implements Inventory {
	private long money = 0;
	private final Map<String, Integer> items = new HashMap<>();
	private final Set<String> unmodifiableItemsSet = Collections.unmodifiableSet(items.keySet());

	@Override
	public Object saveJson() {
		return Map.of(
				"money", money,
				"items", items
		);
	}

	@Override
	public void loadJson(@Nullable Object json) {
		Map<?,?> jsonMap = json instanceof Map<?,?> m ? m : Collections.EMPTY_MAP;

		money = jsonMap.get("money") instanceof Number n ? n.longValue() : 0;

		items.clear();
		if (jsonMap.get("items") instanceof Map<?,?> jsonItems) {
			jsonItems.forEach((key,value)->{
				if (key instanceof String item && value instanceof Number count)
					items.put(item, count.intValue());
			});
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
