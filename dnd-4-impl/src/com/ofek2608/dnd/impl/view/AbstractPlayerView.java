package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.resources.GameData;

public abstract class AbstractPlayerView extends GameData implements PlayerView {
	public AbstractPlayerView(String path) {
		super(Data.REGISTRY, path);
	}
}
