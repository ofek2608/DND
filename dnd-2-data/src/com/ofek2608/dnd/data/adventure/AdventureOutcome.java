package com.ofek2608.dnd.data.adventure;

import com.ofek2608.dnd.utils.ItemList;
import com.ofek2608.dnd.resources.Res;
import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.gim.Gim;

import javax.annotation.Nullable;

public sealed abstract class AdventureOutcome extends LoadableGameData {
	@Res public final float weight = fieldFloat(1);

	private AdventureOutcome(LoadContext ctx) {
		super(ctx);
	}

	public static final class Die extends AdventureOutcome {
		public Die(LoadContext ctx) {
			super(ctx);
		}

		public static boolean validateLoad(Gim data) {
			return data.get("die").getBoolean();
		}
	}

	public static final class Reward extends AdventureOutcome {
		@Res(name = "money") public final float moneyMean = fieldFloat();
		@Res public final float moneyStd = fieldFloat();
		@Res public final ItemList items = field(ItemList.EMPTY);
		@Res public final @Nullable String region = fieldStringP("adventure.");
		@Res public final @Nullable String trap = fieldStringP("trap.");

		public Reward(LoadContext ctx) {
			super(ctx);
		}

		public static boolean validateLoad(Gim data) {
			return !data.get("die").getBoolean();
		}
	}
}
