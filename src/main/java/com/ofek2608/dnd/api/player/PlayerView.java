package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.Lang;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.List;
import java.util.Random;

public interface PlayerView extends Identifiable {
	void buildEmbed(Context context, EmbedBuilder builder);
	ActionRow[] createActionRows(Context context);
	void onClick(Context context, String id);
	default void onSelection(Context context, String id, List<String> values) {}

	class Context {
		public final Player player;
		public final Lang lang;
		public final Random r;

		public Context(Player player) {
			this(player, new Random());
		}

		public Context(Player player, Random r) {
			this.player = player;
			this.lang = player.getSettings().getLanguage();
			this.r = r;
		}

		public String t(String key) {
			return lang.get(key);
		}
	}
}
