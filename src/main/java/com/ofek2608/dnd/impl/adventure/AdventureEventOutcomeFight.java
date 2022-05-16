package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

//TODO
public class AdventureEventOutcomeFight extends AdventureEventOutcomeBase {
	public AdventureEventOutcomeFight(String id) {
		super(id);
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {

	}

	@Override
	public ActionRow[] createActionRows(Player player) {
		return new ActionRow[0];
	}

	@Override
	public void onClick(Player player, String id) {

	}

	@Override
	public void apply(Player player) {

	}
}
