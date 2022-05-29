package com.ofek2608.dnd.impl.pet;

import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.pet.PlayerPet;
import com.ofek2608.dnd.api.player.pet.PlayerPets;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;

public final class PlayerPetsImpl implements PlayerPets {
	private final PlayerPet[] pets;
	private int guardIndex;
	private int assistIndex;

	public PlayerPetsImpl(Player player) {
		pets = new PlayerPet[PET_COUNT];
		for (int i = 0; i < PET_COUNT; i++)
			pets[i] = new PlayerPetImpl(player, this, i);
	}

	@Override
	public void save(GimBuilder builder) {
		builder.child("guardIndex").set(guardIndex);
		for (int i = 0; i < PET_COUNT; i++)
			pets[i].save(builder.child(i));
	}

	@Override
	public void load(Gim data) {
		guardIndex = data.get("guardIndex").getInt(-1);
		for (int i = 0; i < PET_COUNT; i++)
			pets[i].load(data.getChild(i));
	}

	@Override
	public void setGuardIndex(int index) {
		this.guardIndex = index < 0 || PET_COUNT <= index ? -1 : index;
	}

	@Override
	public int getGuardIndex() {
		return guardIndex;
	}

	@Override
	public void setAssistIndex(int index) {
		this.assistIndex = index < 0 || PET_COUNT <= index ? -1 : index;
	}

	@Override
	public int getAssistIndex() {
		return assistIndex;
	}

	@Override
	public PlayerPet get(int index) {
		return pets[index];
	}
}
