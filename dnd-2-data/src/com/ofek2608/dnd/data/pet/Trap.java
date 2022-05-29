package com.ofek2608.dnd.data.pet;

import com.ofek2608.dnd.funcs.WeightsParser;
import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.utils.Weights;

import java.util.Objects;

public final class Trap extends LoadableGameData {
	@Res public final Weights<String> possiblePets = WeightsParser.parseWeights(fieldCtx(), ctx -> "pet." + Objects.requireNonNull(ctx.data.valueString));
	@Res public final float captureTime = fieldFloat(1);//computed by (long)(random.nextExponential() * captureTime)
	@Res public final float breakTime = fieldFloat(1);//computed by (long)(random.nextExponential() * breakTime * pet.breakTimeModifier)

	public Trap(LoadContext ctx) {
		super(ctx);
	}
}
