package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.adventure.AdventureAction;
import com.ofek2608.dnd.api.adventure.AdventureOutcome;
import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.utils.Weight;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.Random;

public class AdventureActionImpl implements AdventureAction {
	private final String id;
	private final boolean hidden;
	private final Cost cost;
	private final Cost ticket;
	private final ButtonStyle buttonStyle;
	private final Weight<AdventureOutcome> outcomes;

	private final String langKey;

	public AdventureActionImpl(String id, boolean hidden, Cost cost, Cost ticket, ButtonStyle buttonStyle,
	                           Weight<AdventureOutcome> outcomes) {
		this.id = id;
		this.hidden = hidden;
		this.cost = cost;
		this.ticket = ticket;
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
	public Cost getTicket() {
		return ticket;
	}

	@Override
	public Button createButton(PlayerView.Context context) {
		Button button = Button.of(buttonStyle, id, context.t(langKey));
		Inventory inv = context.player.getData().getBothInventories();
		return cost.check(inv, false) && ticket.check(inv, false) ? button : hidden ? null : button.asDisabled();
	}

	@Override
	public AdventureOutcome getOutcome(Random r) {
		return outcomes.get(r);
	}
}
