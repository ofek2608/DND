package com.ofek2608.dnd.impl.adventure.view;

import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.impl.adventure.AdventureRespawnValidation;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class AdventureRegionView implements PlayerView {
	private final String id;
	private final boolean escapable;
	private final String descriptionKey;

	public AdventureRegionView(String regionId, boolean escapable) {
		this.id = "view." + regionId;
		this.escapable = escapable;
		this.descriptionKey = "description." + regionId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t(descriptionKey));
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		return new ActionRow[] { ActionRow.of(
				Button.primary("continue", context.t("button.continue")),
				Button.danger("home", context.t(escapable ? "button.return_home" : "button.respawn")),
				Button.secondary("menu", context.t("button.menu"))
		)};
	}

	@Override
	public void onClick(Context context, String id) {
		switch (id) {
			case "continue" -> context.player.continueAdventure();
			case "home" -> {
				if (escapable)
					context.player.goHome();
				else
					context.player.getMessage().setView(AdventureRespawnValidation.INSTANCE);
			}
			case "menu" -> context.player.openMenu();
		}
	}
}
