package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.PlayerViewable;
import com.ofek2608.dnd.api.adventure.AdventureRegion;
import com.ofek2608.dnd.impl.adventure.AdventureHomeView;
import com.ofek2608.dnd.resources.Resources;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.util.Random;

public class MenuView implements PlayerViewable {
	public static final MenuView INSTANCE = new MenuView();
	private MenuView() {}

	@Override
	public String getId() {
		return "view.menu";
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {
		Random r = new Random();
		builder.appendDescription(player.getLanguage().get("description.menu", r));
	}

	@Override
	public ActionRow[] createActionRows(Player player) {
		Random r = new Random();
		Lang lang = player.getLanguage();

		return new ActionRow[] {
				ActionRow.of(
						Button.primary("adventure", lang.get("button.menu.go_adventure", r)),
						Button.primary("inventory", lang.get("button.menu.inventory", r))
				)
		};
	}

	@Override
	public void onClick(Player player, String id) {
		switch (id) {
			case "adventure" -> openAdventure(player);
			case "inventory" -> player.openInventory();
		}
	}

	public static void openAdventure(Player player) {
		@Nullable
		String regionName = player.getRegion();

		if (regionName != null && Resources.get(regionName) instanceof AdventureRegion region)
			player.setView(region);
		else
			player.setView(AdventureHomeView.INSTANCE);
	}
}
