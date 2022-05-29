package com.ofek2608.dnd.utils;

import com.ofek2608.dnd.resources.LoadContext;

import javax.annotation.Nullable;
import java.util.Random;

public final class Roll {
	private final int constant;
	private final int diceCount;
	private final int diceMax;
	private final String asString;

	public Roll(int constant, int diceCount, int diceMax) {
		if (diceMax <= 0) {
			diceCount = 0;
			diceMax = 0;
		} else if (diceMax == 1) {
			constant += diceCount;
			diceCount = 0;
			diceMax = 0;
		} else if (diceCount < 0) {
			diceCount = 0;
			diceMax = 0;
		}

		if (constant < 0)
			constant = 0;


		this.constant = constant;
		this.diceCount = diceCount;
		this.diceMax = diceMax;

		if (diceCount == 0)
			asString = "" + constant;
		else
			asString = (constant > 0 ? constant + "+" : "") + diceCount + "D" + diceMax;
	}

	@Override
	public String toString() {
		return asString;
	}

	public long get(Random r) {
		int result = constant + diceCount;//add dice count because random give number between 0 and max-1
		for (int i = 0; i < diceCount; i++)
			result += r.nextInt(diceMax);
		return result;
	}

	@Nullable
	public static Roll parseCtx(LoadContext ctx) {
		return parseString(ctx.data.valueString);
	}

	@Nullable
	public static Roll parseString(@Nullable String str) {
		if (str == null)
			return null;

		try {
			str = str.replaceAll(" ", "").toUpperCase();

			int plus = str.indexOf('+');
			if (plus <= 0) {
				String[] split = str.split("D");

				if (split.length == 1)
					return new Roll(Integer.parseInt(split[0]), 0, 0);
				if (split.length == 2)
					return new Roll(0, Integer.parseInt(split[0]), Integer.parseInt(split[1]));

				return null;
			}

			int d = str.indexOf('D');

			if (d < plus) {
				return new Roll(
						Integer.parseInt(str.substring(plus + 1)),
						Integer.parseInt(str.substring(0, d)),
						Integer.parseInt(str.substring(d + 1, plus))
				);
			} else {
				return new Roll(
						Integer.parseInt(str.substring(0, plus)),
						Integer.parseInt(str.substring(plus + 1, d)),
						Integer.parseInt(str.substring(d + 1))
				);
			}
		} catch (NumberFormatException ignored) {}
		return null;
	}
}
