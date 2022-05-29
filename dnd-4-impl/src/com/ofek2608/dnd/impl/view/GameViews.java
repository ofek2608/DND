package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.api.player.pet.PlayerPets;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.data.adventure.AdventureAction;
import com.ofek2608.dnd.data.adventure.AdventureEvent;
import com.ofek2608.dnd.data.adventure.AdventureOutcome;
import com.ofek2608.dnd.data.adventure.AdventureRegion;

public final class GameViews {
	private GameViews() {}
	public static void loadClass() {}

	public static final PlayerView MENU, INVENTORY, HOME, RESPAWN_VALIDATION;
	public static final PlayerView PETS;



	static {
		MENU = new MenuView();
		INVENTORY = new InventoryView();
		HOME = new AdventureHomeView();
		RESPAWN_VALIDATION = new AdventureRespawnValidationView();

		PETS = new PetView(-1);
		for (int i = 0; i < PlayerPets.PET_COUNT; i++)
			new PetView(i);

		Object[] gameData = Data.REGISTRY.objs.values().toArray();
		for (Object data : gameData) {
			if (data instanceof AdventureRegion region) {
				AdventureRegionView regionView = new AdventureRegionView(region);
				for (AdventureEvent event : region.events.values()) {
					new AdventureEventView(event);
					for (AdventureAction action : event.actionsArr) {
						for (AdventureOutcome outcome : action.outcomes) {
							if (outcome instanceof AdventureOutcome.Die die)
								new AdventureOutcomeViewDie(die);
							else if (outcome instanceof AdventureOutcome.Reward reward)
								new AdventureOutcomeViewReward(reward, regionView);
						}
					}
				}
			}
		}
	}
}
