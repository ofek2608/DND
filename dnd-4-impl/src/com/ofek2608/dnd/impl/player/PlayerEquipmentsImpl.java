package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.equipment.PlayerEquipmentSlot;
import com.ofek2608.dnd.api.player.equipment.PlayerEquipments;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;

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
	public void save(GimBuilder builder) {
		helmet    .save(builder.child("helmet"    ));
		chestplate.save(builder.child("chestplate"));
		leggings  .save(builder.child("leggings"  ));
		boots     .save(builder.child("boots"     ));
		sword     .save(builder.child("sword"     ));
		wand      .save(builder.child("wand"      ));
		shield    .save(builder.child("shield"    ));
		food      .save(builder.child("food"      ));
	}

	@Override
	public void load(Gim data) {
		helmet    .load(data.get("helmet"    ));
		chestplate.load(data.get("chestplate"));
		leggings  .load(data.get("leggings"  ));
		boots     .load(data.get("boots"     ));
		sword     .load(data.get("sword"     ));
		wand      .load(data.get("wand"      ));
		shield    .load(data.get("shield"    ));
		food      .load(data.get("food"      ));
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
