package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.adventure.AdventureEvent;
import com.ofek2608.dnd.api.adventure.AdventureRegion;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.impl.adventure.view.AdventureRegionView;
import com.ofek2608.dnd.utils.Weight;

import java.util.Random;

public class AdventureRegionImpl implements AdventureRegion {
	private final String id;
	private final String name;
	private final boolean escapable;
	private final Weight<AdventureEvent> events;
	private final PlayerView view;

	public AdventureRegionImpl(String name, boolean escapable, Weight<AdventureEvent> events) {
		this.id = "region." + name;
		this.name = name;
		this.escapable = escapable;
		this.events = events;
		this.view = new AdventureRegionView(id, escapable);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AdventureEvent getEvent(Random r) {
		return events.get(r);
	}

	@Override
	public boolean isEscapable() {
		return escapable;
	}

	@Override
	public PlayerView getView() {
		return view;
	}
}
