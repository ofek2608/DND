package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.adventure.AdventureEventOutcome;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Random;

public abstract class AdventureEventOutcomeBase implements AdventureEventOutcome {
	protected final String id;

	protected final String descriptionKey;

	public AdventureEventOutcomeBase(String id) {
		this.id = id;
		this.descriptionKey = "description." + id;
	}

	@Override
	public String getId() {
		return id;
	}
}