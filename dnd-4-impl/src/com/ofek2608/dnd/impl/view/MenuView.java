package com.ofek2608.dnd.impl.view;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public final class MenuView extends AbstractPlayerView {
	public MenuView() {
		super("view.menu");
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t("description.menu"));
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		return new ActionRow[] {
				ActionRow.of(
						Button.primary("adventure", context.t("button.menu.go_adventure")),
						Button.primary("inventory", context.t("button.menu.inventory")),
						Button.primary("pets", context.t("button.menu.pets"))
				)
		};
	}

	@Override
	public void onClick(Context context, String id) {
		switch (id) {
			case "adventure" -> context.player.openAdventure();
			case "inventory" -> context.player.openInventory();
			case "pets"      -> context.player.openPets();
		}
	}
}
