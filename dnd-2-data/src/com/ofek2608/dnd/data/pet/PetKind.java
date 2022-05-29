package com.ofek2608.dnd.data.pet;

import com.ofek2608.dnd.funcs.WeightsParser;
import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.utils.Weights;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public final class PetKind extends LoadableGameData {
	public static final float CHANGE_RATE_MULTIPLIER = 1f / (1000 * 60 * 60);//once per hour


	@Res public final float breakTimeModifier = fieldFloat();

	@Res public final float maxHealth = fieldFloat();
	@Res public final float maxHunger = fieldFloat();

	@Res public final float healthChangeRate = CHANGE_RATE_MULTIPLIER * fieldFloat();
	@Res public final float hungerChangeRate = CHANGE_RATE_MULTIPLIER * fieldFloat();
	@Res public final float loveChangeRate   = CHANGE_RATE_MULTIPLIER * fieldFloat();

	@Res public final float huntTime = fieldFloat(1);//computed by (long)(random.nextExponential() * huntTime)




	@Res public final Map<String, PetFoodResult> food = fieldCtx().data.childrenStream()
			.collect(Collectors.toMap(child->"item." + child.name, PetFoodResult::new));
	@Res public final @Nullable Weights<String> hunting = WeightsParser.parseNullableWeights(fieldCtx(), a -> "item." + Objects.requireNonNull(a.data.valueString));

	public PetKind(LoadContext ctx) {
		super(ctx);
	}
}
