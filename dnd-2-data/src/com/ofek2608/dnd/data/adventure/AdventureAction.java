package com.ofek2608.dnd.data.adventure;

import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.utils.Cost;
import com.ofek2608.utils.Weights;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public final class AdventureAction extends LoadableGameData {
	@Res public final boolean hidden = fieldBoolean();
	@Res public final Cost cost = field(Cost.EMPTY);
	@Res public final Cost ticket = field(Cost.EMPTY);
	@Res public final ButtonStyle style = field(ButtonStyle.SECONDARY);
	@Res public final AdventureOutcome[] outcomes = fieldNonnull();
	public final Weights<AdventureOutcome> outcomesWeights;

	public AdventureAction(LoadContext ctx) {
		super(ctx);
		outcomesWeights = Weights.createFromFunc(outcomes, outcome->outcome.weight);
	}
}
