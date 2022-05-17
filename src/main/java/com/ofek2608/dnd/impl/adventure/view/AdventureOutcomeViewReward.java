package com.ofek2608.dnd.impl.adventure.view;

import com.ofek2608.dnd.api.adventure.AdventureRegion;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.resources.Resources;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class AdventureOutcomeViewReward implements PlayerView {
	private final String id;
	private final String descriptionKey;
	private final String region;

	public AdventureOutcomeViewReward(String outcomeId, String region) {
		this.id = "view." + outcomeId;
		this.descriptionKey = "description." + outcomeId;
		this.region = region;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t(descriptionKey)
				.replaceAll("%m", "" + context.player.getStats().getLastIncome()));
	}

	//Buttons are from the Region
	@Override
	public ActionRow[] createActionRows(Context context) {
		if (Resources.get(this.region) instanceof AdventureRegion region)
			return region.getView().createActionRows(context);
		return new ActionRow[0];//Shouldn't happen!
	}

	//Buttons are from the Region
	@Override
	public void onClick(Context context, String id) {
		if (Resources.get(this.region) instanceof AdventureRegion region)
			region.getView().onClick(context, id);
	}
}
