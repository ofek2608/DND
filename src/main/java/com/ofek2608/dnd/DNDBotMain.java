package com.ofek2608.dnd;

import com.ofek2608.dnd.bot.DNDBot;
import com.ofek2608.dnd.impl.InventoryView;
import com.ofek2608.dnd.impl.MenuView;
import com.ofek2608.dnd.impl.adventure.AdventureHomeView;
import com.ofek2608.dnd.impl.adventure.AdventureRespawnValidation;
import com.ofek2608.dnd.resources.ResourcePackage;
import com.ofek2608.dnd.resources.Resources;

public class DNDBotMain {
	public static void main(String[] args) {
		ResourcePackage[] packages = ResourcePackage.loadAllPackages();
		Resources.setPackages(packages);

		registerAllNeeded();

		DNDBot.loadClass();
	}

	private static void registerAllNeeded() {
		Resources.register(
				MenuView.INSTANCE,
				InventoryView.INSTANCE,
				AdventureHomeView.INSTANCE,
				AdventureRespawnValidation.INSTANCE
		);
	}
}
