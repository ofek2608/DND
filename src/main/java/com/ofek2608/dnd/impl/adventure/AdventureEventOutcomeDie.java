package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.impl.MenuView;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Random;

public class AdventureEventOutcomeDie extends AdventureEventOutcomeBase {
	public AdventureEventOutcomeDie(String id) {
		super(id);
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {
		Random r = new Random();
		Lang lang = player.getLanguage();

		builder.appendDescription(lang.get(descriptionKey, r));
		builder.appendDescription("\n");
		builder.appendDescription(lang.get("description.adventure.died", r)
				.replaceAll("%t", "<t:" + player.getReviveTime() / 1000 + ":R>"));
	}

	@Override
	public ActionRow[] createActionRows(Player player) {
		Random r = new Random();
		Lang lang = player.getLanguage();


		return new ActionRow[] { ActionRow.of(
				Button.secondary("menu", lang.get("button.menu", r))
		)};
	}

	@Override
	public void onClick(Player player, String id) {
		if (id.equals("menu"))
			player.setView(MenuView.INSTANCE);
	}

	@Override
	public void apply(Player player) {
		 player.kill();
	}
}
