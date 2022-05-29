package com.ofek2608.dnd.api.player.pet;

import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.data.pet.PetKind;
import com.ofek2608.dnd.data.pet.Trap;

import javax.annotation.Nullable;
import java.util.Random;

public interface PlayerPet extends Savable {
	int getIndex();

	//======
	// Trap
	//======

	@Nullable Trap getTrap();
	long captureTime();
	long breakTime();


	//=====
	// Pet
	//=====

	@Nullable PetKind getKind();
	boolean isTame();
	float getHealth();
	float getHunger();
	float getLove();

	String getName();
	void setName(String name);

	long getNextHunt();

	//=========
	// Actions
	//=========

	long getLastUpdate();
	void update();

	void setTrap(Trap trap, Random r);
	void openTrap();
	void feed(String item);
	void discard();


	void guard();
	void assist();
	void hunt();
	void retire();
}
