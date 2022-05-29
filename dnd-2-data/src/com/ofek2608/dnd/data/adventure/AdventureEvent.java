package com.ofek2608.dnd.data.adventure;

import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;

import java.util.stream.Stream;

public final class AdventureEvent extends LoadableGameData {
	@Res public final float weight = fieldFloat(1);
	@Res public final AdventureAction[][] actions = fieldNonnull();
	public final AdventureAction[] actionsArr;

	public AdventureEvent(LoadContext ctx) {
		super(ctx);
		actionsArr = Stream.of(actions).flatMap(Stream::of).toArray(AdventureAction[]::new);
	}
}
