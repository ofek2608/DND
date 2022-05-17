package com.ofek2608.dnd.impl.adventure.view;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.adventure.AdventureEvent;
import com.ofek2608.dnd.api.adventure.AdventureAction;
import com.ofek2608.dnd.api.adventure.AdventureOutcome;
import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.PlayerView;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import javax.annotation.Nullable;

public class AdventureEventView implements PlayerView {
	private final String id;
	private final AdventureEvent event;
	private final String descriptionKey;

	public AdventureEventView(AdventureEvent event) {
		String adventureId = event.getId();
		this.id = "view." + adventureId;
		this.event = event;
		this.descriptionKey = "description." + adventureId;
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
		return event.createActionRows(context);
	}

	@Override
	public void onClick(Context context, String id) {
		int index = getIndexFromButtonId(id);
		@Nullable
		AdventureAction action = event.getAction(index);
		if (action == null)
			return;

		Cost cost = action.getCost();
		Cost ticket = action.getCost();
		Inventory inv = context.player.getData().getBothInventories();
		if (!ticket.check(inv, false) || !cost.check(inv, true))
			return;

		AdventureOutcome outcome = action.getOutcome(context.r);
		outcome.apply(context.player, context.r);
		context.player.getMessage().setView(outcome.getView());
	}

	private static int getIndexFromButtonId(String id) {
		try {
			int dot = id.lastIndexOf('.');
			if (dot > 0)
				return Integer.parseInt(id.substring(dot + 1));
		} catch (NumberFormatException ignored) {}
		return -1;
	}
}
