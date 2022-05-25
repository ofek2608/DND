package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.PlayerEquipmentSlot;
import com.ofek2608.dnd.api.player.PlayerEquipments;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class PlayerEquipmentsImpl implements PlayerEquipments {
	private final PlayerEquipmentSlot helmet, chestplate, leggings, boots, sword, wand, shield, food;

	public PlayerEquipmentsImpl(Inventory inventory) {
		this.helmet     = new PlayerEquipmentSlotImpl(inventory);
		this.chestplate = new PlayerEquipmentSlotImpl(inventory);
		this.leggings   = new PlayerEquipmentSlotImpl(inventory);
		this.boots      = new PlayerEquipmentSlotImpl(inventory);
		this.sword      = new PlayerEquipmentSlotImpl(inventory);
		this.wand       = new PlayerEquipmentSlotImpl(inventory);
		this.shield     = new PlayerEquipmentSlotImpl(inventory);
		this.food       = new PlayerEquipmentSlotImpl(inventory);
	}

	@Override
	public Object saveJson() {
		return Map.of(
				"helmet"    , helmet    .saveJson(),
				"chestplate", chestplate.saveJson(),
				"leggings"  , leggings  .saveJson(),
				"boots"     , boots     .saveJson(),
				"sword"     , sword     .saveJson(),
				"wand"      , wand      .saveJson(),
				"shield"    , shield    .saveJson(),
				"food"      , food      .saveJson()
		);
	}

	@Override
	public void loadJson(@Nullable Object json) {
		Map<?,?> jsonMap = json instanceof Map<?,?> m ? m : Collections.EMPTY_MAP;
		helmet    .loadJson(jsonMap.get("helmet"    ));
		chestplate.loadJson(jsonMap.get("chestplate"));
		leggings  .loadJson(jsonMap.get("leggings"  ));
		boots     .loadJson(jsonMap.get("boots"     ));
		sword     .loadJson(jsonMap.get("sword"     ));
		wand      .loadJson(jsonMap.get("wand"      ));
		shield    .loadJson(jsonMap.get("shield"    ));
		food      .loadJson(jsonMap.get("food"      ));
	}

	@Override public PlayerEquipmentSlot getHelmet    () { return helmet    ; }
	@Override public PlayerEquipmentSlot getChestplate() { return chestplate; }
	@Override public PlayerEquipmentSlot getLeggings  () { return leggings  ; }
	@Override public PlayerEquipmentSlot getBoots     () { return boots     ; }
	@Override public PlayerEquipmentSlot getSword     () { return sword     ; }
	@Override public PlayerEquipmentSlot getWand      () { return wand      ; }
	@Override public PlayerEquipmentSlot getShield    () { return shield    ; }
	@Override public PlayerEquipmentSlot getFood      () { return food      ; }
}
