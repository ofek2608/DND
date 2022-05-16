package com.ofek2608.dnd.api.adventure;

import com.ofek2608.dnd.api.Named;
import com.ofek2608.dnd.api.PlayerViewable;

import java.util.Random;

public interface AdventureRegion extends PlayerViewable, Named {
	AdventureEvent getEvent(Random r);
	boolean isEscapable();
}
