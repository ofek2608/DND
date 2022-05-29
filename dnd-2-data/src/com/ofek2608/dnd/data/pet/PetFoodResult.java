package com.ofek2608.dnd.data.pet;

import com.ofek2608.gim.Gim;

public final class PetFoodResult {
	public final float health, hunger, love;

	public PetFoodResult(float health, float hunger, float love) {
		this.health = health;
		this.hunger = hunger;
		this.love   = love  ;
	}

	public PetFoodResult(Gim data) {
		this.health = data.get("health").getFloat();
		this.hunger = data.get("hunger").getFloat();
		this.love   = data.get("love"  ).getFloat();
	}
}
