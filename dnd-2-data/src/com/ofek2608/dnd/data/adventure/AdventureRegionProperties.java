package com.ofek2608.dnd.data.adventure;

import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;

public final class AdventureRegionProperties extends LoadableGameData {
	@Res public final boolean escapable = fieldBoolean(true);

	public AdventureRegionProperties(LoadContext ctx) {
		super(ctx);
	}
}
