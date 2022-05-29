package com.ofek2608.dnd.api.player.pet;

import com.ofek2608.dnd.api.Savable;

public interface PlayerPets extends Savable {
	int PET_COUNT = 10;

	int getGuardIndex();
	void setGuardIndex(int index);
	int getAssistIndex();
	void setAssistIndex(int index);

	PlayerPet get(int index);

	default void update() {
		for (int i = 0; i < PET_COUNT; i++) {
			get(i).update();
		}
	}
}
