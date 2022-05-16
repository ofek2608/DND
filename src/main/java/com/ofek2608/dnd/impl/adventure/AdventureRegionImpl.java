package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.adventure.AdventureEvent;
import com.ofek2608.dnd.api.adventure.AdventureRegion;
import com.ofek2608.dnd.impl.MenuView;
import com.ofek2608.dnd.utils.Weight;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Random;

public class AdventureRegionImpl implements AdventureRegion {
	private final String id;
	private final String name;
	private final boolean escapable;
	private final Weight<AdventureEvent> events;

	private final String descriptionKey;

	public AdventureRegionImpl(String name, boolean escapable, Weight<AdventureEvent> events) {
		this.id = "region." + name;
		this.name = name;
		this.escapable = escapable;
		this.events = events;

		this.descriptionKey = "description." + id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {
		Random r = new Random();
		builder.appendDescription(player.getLanguage().get(descriptionKey, r));
	}

	@Override
	public ActionRow[] createActionRows(Player player) {
		Random r = new Random();
		Lang lang = player.getLanguage();


		return new ActionRow[] { ActionRow.of(
				Button.primary("continue", lang.get("button.continue", r)),
				Button.danger("home", lang.get(escapable ? "button.return_home" : "button.respawn", r)),
				Button.secondary("menu", lang.get("button.menu", r))
		)};
	}

	@Override
	public void onClick(Player player, String id) {
		switch (id) {
			case "continue" -> player.continueAdventure();
			case "home" -> {
				if (escapable)
					player.goHome();
				else
					player.setView(AdventureRespawnValidation.INSTANCE);
			}
			case "menu" -> player.setView(MenuView.INSTANCE);
		}
	}

	@Override
	public AdventureEvent getEvent(Random r) {
		return events.get(r);
	}

	@Override
	public boolean isEscapable() {
		return escapable;
	}
}
