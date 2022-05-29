package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.data.adventure.AdventureOutcome;
import net.dv8tion.jda.api.entities.User;

import java.util.Random;

public interface Player extends Savable {
	User getUser();
	PlayerSettings getSettings();
	PlayerMessage getMessage();
	PlayerData getData();
	PlayerStats getStats();


	void openMenu();
	void openAdventure();
	void openInventory();
	void openPets();
	void continueAdventure(Random r);
	void goHome();
	void respawn();

	void runAdventureOutcome(AdventureOutcome outcome, Random r);
}
