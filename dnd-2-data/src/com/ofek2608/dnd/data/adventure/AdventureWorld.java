package com.ofek2608.dnd.data.adventure;

import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;

import java.util.Map;

public final class AdventureWorld extends LoadableGameData {
	@Res(mapKind = AdventureRegion.class)
	public final Map<String, AdventureRegion> regions = fieldNonnull();

	public AdventureWorld(LoadContext ctx) {
		super(ctx);
	}
}
