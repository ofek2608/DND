package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.player.PlayerView;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class AdventureRespawnValidation implements PlayerView {
	public static final AdventureRespawnValidation INSTANCE = new AdventureRespawnValidation();
	private AdventureRespawnValidation() {}

	@Override
	public String getId() {
		return "view.adventure_respawn_validation";
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t("description.adventure.validate_respawn"));
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
