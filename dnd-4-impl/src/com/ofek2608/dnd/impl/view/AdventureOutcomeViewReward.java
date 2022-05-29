package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.data.adventure.AdventureOutcome;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public final class AdventureOutcomeViewReward extends AbstractPlayerView {
	private final String descriptionKey;
	private final AdventureRegionView region;

	public AdventureOutcomeViewReward(AdventureOutcome.Reward outcome, AdventureRegionView region) {
		super(outcome.path + ".view");
		this.descriptionKey = "description." + outcome.path;
		this.region = Data.get(outcome.region + ".view") instanceof AdventureRegionView view ? view : region;
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t(descriptionKey)
				.replaceAll("%m", "" + context.player.getStats().getLastIncome()));
	}

	//Buttons are from the Region
	@Override
	public ActionRow[] createActionRows(Context context) {
		return region.createActionRows(context);
	}

	//Buttons are from the Region
	@Override
	public void onClick(Context context, String id) {
		region.onClick(context, id);
	}
}
