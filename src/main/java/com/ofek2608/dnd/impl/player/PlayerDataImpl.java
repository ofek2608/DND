package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerData;
import com.ofek2608.dnd.api.player.PlayerEquipments;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataImpl implements PlayerData {
	private final Player player;
	private final Inventory inventory;
	private final Inventory backpack;
	private final Inventory bothInventories;
	private final PlayerEquipments equipments;
	@Nullable private String region;
	private float health;

	public PlayerDataImpl(Player player) {
		this.player = player;
		this.inventory = new InventoryImpl();
		this.backpack = new InventoryImpl();
		this.bothInventories = new BothInventories(inventory, backpack);
		this.equipments = new PlayerEquipmentsImpl(bothInventories);
		this.region = null;
		this.health = 1;
	}

	@Override
	public Object saveJson() {
		Map<String,Object> result = new HashMap<>();
		result.put("inventory" , inventory .saveJson());
		result.put("backpack"  , backpack  .saveJson());
		result.put("equipments", equipments.saveJson());
		if (region != null)
			result.put("region", region);
		result.put("health", health);
		return result;
	}

	@Override
	public void loadJson(@Nullable Object json) {
		Map<?,?> jsonMap = json instanceof Map<?,?> m ? m : Collections.EMPTY_MAP;
		inventory .loadJson(jsonMap.get("inventory" ));
		backpack  .loadJson(jsonMap.get("backpack"  ));
		equipments.loadJson(jsonMap.get("equipments"));
		region = jsonMap.get("region") instanceof String s ? s : null;
		health = jsonMap.get("health") instanceof Number n ? n.floatValue() : 1;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public Inventory getBackpack() {
		return backpack;
	}

	@Override
	public Inventory getBothInventories() {
		return bothInventories;
	}

	@Override
	public PlayerEquipments getEquipment() {
		return equipments;
	}

	@Override
	public void setRegion(@Nullable String region) {
		this.region = region;
	}

	@Nullable
	@Override
	public String getRegion() {
		return region;
	}

	@Override
	public void setHealth(float health) {
		this.health = health;
	}

	@Override
	public float getHealth() {
		return health;
	}


	@Override
	public void damage(float value) {
		float newHealth = this.health - value;
		if (newHealth < 0) {
			kill();
			return;
		}
		health = newHealth;
		player.getStats().setLastDamage(value);
	}

	@Override
	public void kill() {
		player.getStats().setLastDeath(System.currentTimeMillis());
		health = 1;
		region = null;
		backpack.clear();
	}

	@Override
	public long getReviveTime() {
		return player.getStats().getLastDeath() + REVIVE_TIME;
	}
}
