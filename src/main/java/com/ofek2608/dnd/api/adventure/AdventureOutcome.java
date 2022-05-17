package com.ofek2608.dnd.api.adventure;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;

import java.util.Random;

public interface AdventureOutcome extends Identifiable {
	void apply(Player player, Random r);
	PlayerView getView();
}
