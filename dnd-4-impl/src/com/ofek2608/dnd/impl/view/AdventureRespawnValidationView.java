package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.api.player.Inventory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Comparator;
import java.util.List;

public final class AdventureRespawnValidationView extends AbstractPlayerView {
	public AdventureRespawnValidationView() {
		super("view.adventure_respawn_validation");
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t("description.adventure.validate_respawn"));
		Inventory inventory = context.player.getData().getBackpack();

		boolean hasNothing = true;

		long money = inventory.getMoney();
		if (money > 0) {
			hasNothing = false;
			builder.appendDescription(context.t("description.adventure.validate_respawn.money").replaceAll("%c", "" + money));
		}

		List<String> items = inventory.getItems().stream().sorted(Comparator.comparingInt(item->-inventory.getItem(item))).limit(10).toList();

		if (items.size() > 0) {
			for (String item : items) {
				builder.appendDescription(context.t("description.adventure.validate_respawn.item")
						.replaceAll("%n", context.t(item))
						.replaceAll("%c", "" + inventory.getItem(item))
				);
			}
			int more = inventory.getItems().size() - items.size();
			if (more > 0)
				builder.appendDescription(context.t("description.adventure.validate_respawn.more").replaceAll("%c", "" + more));
		}

		if (hasNothing) {
			builder.appendDescription(context.t("description.adventure.validate_respawn.nothing"));
		}
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		return new ActionRow[] {
				ActionRow.of(
						Button.danger("respawn", context.t("button.respawn")),
						Button.secondary("cancel", context.t("button.cancel"))
				)
		};
	}

	@Override
	public void onClick(Context context, String id) {
		switch (id) {
			case "respawn" -> context.player.respawn();
			case "cancel" -> context.player.openAdventure();
		}
	}
}
