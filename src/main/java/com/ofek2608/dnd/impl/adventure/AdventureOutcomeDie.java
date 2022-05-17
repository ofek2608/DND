package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.adventure.AdventureOutcome;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.impl.adventure.view.AdventureOutcomeViewDie;

import java.util.Random;

public class AdventureOutcomeDie implements AdventureOutcome {
	private final String id;
	private final PlayerView view;

	public AdventureOutcomeDie(String id) {
		this.id = id;
		this.view = new AdventureOutcomeViewDie(id);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void apply(Player player, Random r) {
		 player.getData().getHealth().kill();
	}

	@Override
	public PlayerView getView() {
		return view;
	}
}
