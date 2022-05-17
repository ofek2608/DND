package com.ofek2608.dnd.api.adventure;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.Named;
import com.ofek2608.dnd.api.player.PlayerView;

import java.util.Random;

public interface AdventureRegion extends Identifiable, Named {
	AdventureEvent getEvent(Random r);
	boolean isEscapable();
	PlayerView getView();
}
