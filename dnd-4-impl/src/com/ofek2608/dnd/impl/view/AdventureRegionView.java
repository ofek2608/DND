package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.data.adventure.AdventureRegion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public final class AdventureRegionView extends AbstractPlayerView {
	private final boolean escapable;
	private final String descriptionKey;

	public AdventureRegionView(AdventureRegion region) {
		super(region.path + ".view");
		this.escapable = region.properties.escapable;
		this.descriptionKey = "description." + region.path;
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
			case "continue" -> context.player.continueAdventure(context.r);
			case "home" -> {
				if (escapable)
					context.player.goHome();
				else
					context.player.getMessage().setView(GameViews.RESPAWN_VALIDATION);
			}
			case "menu" -> context.player.openMenu();
		}
	}
}
