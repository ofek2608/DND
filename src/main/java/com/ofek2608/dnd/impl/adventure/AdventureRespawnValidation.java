package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.PlayerViewable;
import com.ofek2608.dnd.impl.MenuView;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Random;

public class AdventureRespawnValidation implements PlayerViewable {
	public static final AdventureRespawnValidation INSTANCE = new AdventureRespawnValidation();
	private AdventureRespawnValidation() {}

	@Override
	public String getId() {
		return "view.adventure_respawn_validation";
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {
		Random r = new Random();
		Lang lang = player.getLanguage();
		builder.appendDescription(lang.get("description.adventure.validate_respawn",r));
	}

	@Override
	public ActionRow[] createActionRows(Player player) {
		Random r = new Random();
		Lang lang = player.getLanguage();

		return new ActionRow[] {
				ActionRow.of(
						Button.danger("respawn", lang.get("button.respawn",r)),
						Button.secondary("cancel", lang.get("button.cancel",r))
				)
		};
	}

	@Override
	public void onClick(Player player, String id) {
		switch (id) {
			case "respawn" -> player.respawn();
			case "cancel" -> MenuView.openAdventure(player);
		}
	}
}
