package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.adventure.AdventureEvent;
import com.ofek2608.dnd.api.adventure.AdventureEventAction;
import com.ofek2608.dnd.api.adventure.AdventureEventOutcome;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class AdventureEventImpl implements AdventureEvent {
	private final String id;
	private final String descriptionKey;
	private final AdventureEventAction[][] actionsTable;
	private final AdventureEventAction[] actions;

	public AdventureEventImpl(String id, AdventureEventAction[][] actionsTable) {
		this.id = id;
		this.descriptionKey = "description." + id;
		this.actionsTable = actionsTable;
		this.actions = Stream.of(actionsTable).flatMap(Stream::of).toArray(AdventureEventAction[]::new);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {
		Random r = new Random();
		builder.appendDescription(player.getLanguage().get(descriptionKey, r));
	}

	@Override
	public ActionRow[] createActionRows(Player player) {
		Random r = new Random();
		return Stream.of(actionsTable)
				.map(row->createActionRow(player, r, row))
				.filter(Objects::nonNull)
				.toArray(ActionRow[]::new);
	}

	@Nullable
	private ActionRow createActionRow(Player player, Random r, AdventureEventAction[] row) {
		List<Button> buttons = Stream.of(row)
				.map(action -> action.createButton(player, r))
				.filter(Objects::nonNull)
				.toList();
		return buttons.size() > 0 ? ActionRow.of(buttons) : null;
	}

	@Override
	public void onClick(Player player, String id) {
		int dot = id.lastIndexOf('.');
		if (dot < 0)
			return;


		int index;
		try {
			index = Integer.parseInt(id.substring(dot + 1));
		} catch (NumberFormatException e) {
			return;
		}

		if (index > actions.length)
			return;

		AdventureEventAction action = actions[index];
		if (!action.getId().equals(id))
			return;

		Cost cost = action.getCost();
		if (!cost.check(player, true))
			return;

		Random r = new Random();
		AdventureEventOutcome outcome = action.getOutcome(r);
		outcome.apply(player);
		player.setView(outcome);
	}
}
