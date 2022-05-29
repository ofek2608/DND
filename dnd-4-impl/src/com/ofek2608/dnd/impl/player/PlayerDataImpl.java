package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerData;
import com.ofek2608.dnd.api.player.equipment.PlayerEquipments;
import com.ofek2608.dnd.api.player.pet.PlayerPets;
import com.ofek2608.dnd.impl.pet.PlayerPetsImpl;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;
import org.jetbrains.annotations.Nullable;

public class PlayerDataImpl implements PlayerData {
	private final Player player;
	private final Inventory inventory;
	private final Inventory backpack;
	private final Inventory bothInventories;
	private final PlayerEquipments equipments;
	private final PlayerPets pets;
	@Nullable private String region;
	private float health;

	public PlayerDataImpl(Player player) {
		this.player = player;
		this.inventory = new InventoryImpl();
		this.backpack = new InventoryImpl();
		this.bothInventories = new BothInventories(inventory, backpack);
		this.equipments = new PlayerEquipmentsImpl(bothInventories);
		this.pets = new PlayerPetsImpl(player);
		this.region = null;
		this.health = 1;
	}

	@Override
	public void save(GimBuilder builder) {
		inventory .save(builder.child("inventory" ));
		backpack  .save(builder.child("backpack"  ));
		equipments.save(builder.child("equipments"));
		builder.child("region").set(region);
		builder.child("health").set(health);
	}

	@Override
	public void load(Gim data) {
		inventory .load(data.get("inventory" ));
		backpack  .load(data.get("backpack"  ));
		equipments.load(data.get("equipments"));
		region = data.get("region").valueString;
		health = data.get("health").getFloat(1);
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
	public PlayerPets getPets() {
		return pets;
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
