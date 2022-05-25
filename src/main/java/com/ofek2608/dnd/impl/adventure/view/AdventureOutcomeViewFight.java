package com.ofek2608.dnd.impl.adventure.view;

import com.ofek2608.dnd.api.enemy.EnemyType;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.impl.adventure.AdventureOutcomeFight;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.Nullable;

public class AdventureOutcomeViewFight implements PlayerView {
	private final String id;
	private final EnemyType enemy;
	private final AdventureOutcomeFight winning;
	@Nullable private final AdventureOutcomeFight retreating;

	public AdventureOutcomeViewFight(String outcomeId, EnemyType enemy, AdventureOutcomeFight winning,
	                                 @Nullable AdventureOutcomeFight retreating) {
		this.id = "view." + outcomeId;
		this.enemy = enemy;
		this.winning = winning;
		this.retreating = retreating;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		//TODO
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		//TODO
		return new ActionRow[0];
	}

	@Override
	public void onClick(Context context, String id) {
		//TODO
	}

	private static final class Data {
		private int health;
		@Nullable
		private String lastAttack;

		public Data(String str) {
			String[] split = str.split(";");
			if (split.length != 2)
				return;

			try {
				health = Integer.parseInt(split[0]);
			} catch (NumberFormatException ignored) {}
			lastAttack = split[1];
		}

		@Override
		public String toString() {
			return health + ";" + lastAttack;
		}
	}
}
