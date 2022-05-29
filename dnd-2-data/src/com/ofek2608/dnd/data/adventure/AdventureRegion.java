package com.ofek2608.dnd.data.adventure;

import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.Res;
import com.ofek2608.utils.Weights;

import java.util.Map;

public final class AdventureRegion extends LoadableGameData {
	@Res(name = "_properties") public final AdventureRegionProperties properties = fieldNonnull();
	@Res(mapKind = AdventureEvent.class)
	public final Map<String, AdventureEvent> events = fieldNonnull();
	public final Weights<AdventureEvent> eventWeights;

	public AdventureRegion(LoadContext ctx) {
		super(ctx);
		eventWeights = Weights.createFromFunc(events.values(), event->event.weight);
	}
}
