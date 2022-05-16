package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.adventure.AdventureEventAction;
import com.ofek2608.dnd.api.adventure.AdventureEventOutcome;
import com.ofek2608.dnd.utils.Weight;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.Random;

public class AdventureEventActionImpl implements AdventureEventAction {
	private final String id;
	private final boolean hidden;
	private final Cost cost;
	private final ButtonStyle buttonStyle;
	private final Weight<AdventureEventOutcome> outcomes;

	private final String langKey;

	public AdventureEventActionImpl(String id, boolean hidden, Cost cost, ButtonStyle buttonStyle,
	                                Weight<AdventureEventOutcome> outcomes) {
		this.id = id;
		this.hidden = hidden;
		this.cost = cost;
		this.buttonStyle = buttonStyle;
		this.outcomes = outcomes;

		this.langKey = "button." + id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Cost getCost() {
		return cost;
	}

	@Override
	public Button createButton(Player player, Random r) {
		Button button = Button.of(buttonStyle, id, player.getLanguage().get(langKey, r));
		return cost.check(player, false) ? button : hidden ? null : button.asDisabled();
	}

	@Override
	public AdventureEventOutcome getOutcome(Random r) {
		return outcomes.get(r);
	}
}
