package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.impl.PlayerImpl;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;

public interface PlayerNew extends Savable {

	static Player getPlayer(User user) {
		return PlayerImpl.getPlayer(user);
	}

	User getUser();
	PlayerSettings getSettings();
	PlayerMessage getMessage();
	PlayerData getData();
	PlayerStats getStats();


	void continueAdventure();
	void goHome();
	void respawn();
	void openInventory();
}
