package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.data.adventure.AdventureOutcome;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public final class AdventureOutcomeViewDie extends AbstractPlayerView {
	private final String descriptionKey;

	public AdventureOutcomeViewDie(AdventureOutcome.Die outcome) {
		super(outcome.path + ".view");
		this.descriptionKey = "description." + outcome.path;
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t(descriptionKey));
		builder.appendDescription("\n");
		builder.appendDescription(context.t("description.adventure.died")
				.replaceAll("%t", "<t:" + context.player.getData().getReviveTime() / 1000 + ":R>"));
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		return new ActionRow[] { ActionRow.of(
				Button.secondary("menu", context.t("button.menu"))
		)};
	}

	@Override
	public void onClick(Context context, String id) {
		if (id.equals("menu"))
			context.player.openMenu();
	}
}
