package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.impl.player.PlayerImpl;
import net.dv8tion.jda.api.entities.User;

public interface Player extends Savable {
	static Player getPlayer(User user) {
		return PlayerImpl.getPlayer(user);
	}

	User getUser();
	PlayerSettings getSettings();
	PlayerMessage getMessage();
	PlayerData getData();
	PlayerStats getStats();


	void openMenu();
	void openAdventure();
	void openInventory();
	void continueAdventure();
	void goHome();
	void respawn();
}
