package com.ofek2608.dnd.api.adventure;

import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.PlayerViewable;

public interface AdventureEventOutcome extends PlayerViewable {
	void apply(Player player);
}
