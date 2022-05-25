package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerData;
import com.ofek2608.dnd.api.player.PlayerEquipments;
import com.ofek2608.dnd.api.player.PlayerHealth;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataImpl implements PlayerData {
	private final Inventory inventory;
	private final Inventory backpack;
	private final Inventory bothInventories;
	private final PlayerHealth health;
	private final PlayerEquipments equipments;
	@Nullable private String region;

	public PlayerDataImpl(Player player) {
		this.inventory = new InventoryImpl();
		this.backpack = new InventoryImpl();
		this.bothInventories = new BothInventories(inventory, backpack);
		this.health = new PlayerHealthImpl(player);
		this.equipments = new PlayerEquipmentsImpl(bothInventories);
	}

	@Override
	public Object saveJson() {
		Map<String,Object> result = new HashMap<>();
		result.put("inventory" , inventory .saveJson());
		result.put("backpack"  , backpack  .saveJson());
		result.put("health"    , health    .saveJson());
		result.put("equipments", equipments.saveJson());
		if (region != null)
			result.put("region", region);
		return result;
	}

	@Override
	public void loadJson(@Nullable Object json) {
		Map<?,?> jsonMap = json instanceof Map<?,?> m ? m : Collections.EMPTY_MAP;
		inventory .loadJson(jsonMap.get("inventory" ));
		backpack  .loadJson(jsonMap.get("backpack"  ));
		health    .loadJson(jsonMap.get("health"    ));
		equipments.loadJson(jsonMap.get("equipments"));
		region = jsonMap.get("region") instanceof String s ? s : null;
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
	public PlayerHealth getHealth() {
		return health;
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
}
