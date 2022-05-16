package com.ofek2608.dnd.api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.List;

public interface PlayerViewable extends Identifiable {
	void buildEmbed(Player player, EmbedBuilder builder);
	ActionRow[] createActionRows(Player player);
	void onClick(Player player, String id);

	default void onSelection(Player player, String id, List<String> values) {}
}
