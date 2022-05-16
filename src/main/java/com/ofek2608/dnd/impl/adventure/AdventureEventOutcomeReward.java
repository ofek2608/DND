package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.PlayerViewable;
import com.ofek2608.dnd.impl.ItemList;
import com.ofek2608.dnd.resources.Resources;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.Random;

public class AdventureEventOutcomeReward extends AdventureEventOutcomeBase {
	private final float moneyMean;
	private final float moneyStd;
	private final String region;
	private final ItemList items;

	public AdventureEventOutcomeReward(String id, float moneyMean, float moneyStd, String region, ItemList items) {
		super(id);
		this.moneyMean = moneyMean;
		this.moneyStd = moneyStd;
		this.region = region;
		this.items = items;
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {
		Random r = new Random();
		Lang lang = player.getLanguage();

		builder.appendDescription(lang.get(descriptionKey, r)
				.replaceAll("%m", "" + player.getLastIncome()));
	}

	//ActionRow and button are from the Region
	@Override
	public ActionRow[] createActionRows(Player player) {
		if (Resources.get(this.region) instanceof PlayerViewable viewable)
			return viewable.createActionRows(player);
		return new ActionRow[0];//Shouldn't happen!
	}

	//ActionRow and button are from the Region
	@Override
	public void onClick(Player player, String id) {
		if (Resources.get(this.region) instanceof PlayerViewable viewable)
			viewable.onClick(player, id);
	}

	@Override
	public void apply(Player player) {
		Random random = new Random();

		long money = Math.max(0, (long)random.nextGaussian(moneyMean, moneyStd));
		player.addMoney(money);
		player.setLastIncome(money);

		player.setRegion(region);
		items.add(player);
	}
}
