package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.adventure.AdventureEvent;
import com.ofek2608.dnd.api.adventure.AdventureAction;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.impl.adventure.view.AdventureEventView;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AdventureEventImpl implements AdventureEvent {
	private final String id;
	private final AdventureAction[][] actionsTable;
	private final AdventureAction[] actions;
	private final PlayerView view;

	public AdventureEventImpl(String id, AdventureAction[][] actionsTable) {
		this.id = id;
		this.actionsTable = actionsTable;
		this.actions = Stream.of(actionsTable).flatMap(Stream::of).toArray(AdventureAction[]::new);
		this.view = new AdventureEventView(this);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ActionRow[] createActionRows(PlayerView.Context context) {
		return Stream.of(actionsTable)
				.map(row->createActionRow(context, row))
				.filter(Objects::nonNull)
				.toArray(ActionRow[]::new);
	}

	@Nullable
	private ActionRow createActionRow(PlayerView.Context context, AdventureAction[] row) {
		List<Button> buttons = Stream.of(row)
				.map(action -> action.createButton(context))
				.filter(Objects::nonNull)
				.toList();
		return buttons.size() > 0 ? ActionRow.of(buttons) : null;
	}

	@Override
	public @Nullable
	AdventureAction getAction(int index) {
		return 0 <= index && index < actions.length ? actions[index] : null;
	}

	@Override
	public PlayerView getView() {
		return view;
	}
}
