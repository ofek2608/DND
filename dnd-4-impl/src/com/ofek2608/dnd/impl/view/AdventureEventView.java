package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.data.adventure.AdventureAction;
import com.ofek2608.dnd.data.adventure.AdventureEvent;
import com.ofek2608.dnd.data.adventure.AdventureOutcome;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class AdventureEventView extends AbstractPlayerView {
	private final AdventureEvent event;
	private final String descriptionKey;

	public AdventureEventView(AdventureEvent event) {
		super(event.path + ".view");
		this.event = event;
		this.descriptionKey = "description." + event.path;
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		builder.appendDescription(context.t(descriptionKey));
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		return Stream.of(event.actions)
				.map(row->createActionRow(context, row))
				.filter(Objects::nonNull)
				.toArray(ActionRow[]::new);
	}

	@Nullable
	private ActionRow createActionRow(Context context, AdventureAction[] row) {
		List<Button> buttons = Stream.of(row)
				.map(action -> createButton(context, action))
				.filter(Objects::nonNull)
				.toList();
		return buttons.size() > 0 ? ActionRow.of(buttons) : null;
	}

	@Nullable
	private Button createButton(Context context, AdventureAction action) {
		Button button = Button.of(action.style, action.path, context.t("button." + action.path));
		Inventory inv = context.player.getData().getBothInventories();
		return inv.checkCost(action.cost, false) && inv.checkCost(action.ticket, false) ? button : action.hidden ? null : button.asDisabled();
	}


	@Override
	public void onClick(Context context, String id) {
		if (!(Data.get(id) instanceof AdventureAction action))
			return;

		Inventory inv = context.player.getData().getBothInventories();
		if (!inv.checkCost(action.ticket, false) || !inv.checkCost(action.cost, true))
			return;

		AdventureOutcome outcome = action.outcomesWeights.get(context.r);
		context.player.runAdventureOutcome(outcome, context.r);

		if (Data.get(outcome.path + ".view") instanceof PlayerView view)
			context.player.getMessage().setView(view);
	}
}
