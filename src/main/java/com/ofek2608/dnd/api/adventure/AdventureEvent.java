package com.ofek2608.dnd.api.adventure;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.player.PlayerView;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import javax.annotation.Nullable;

public interface AdventureEvent extends Identifiable {
	ActionRow[] createActionRows(PlayerView.Context context);
	@Nullable
	AdventureAction getAction(int index);
	PlayerView getView();
}
