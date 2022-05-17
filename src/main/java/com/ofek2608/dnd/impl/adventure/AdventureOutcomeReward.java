package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.adventure.AdventureOutcome;
import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.impl.ItemList;
import com.ofek2608.dnd.impl.adventure.view.AdventureOutcomeViewReward;

import java.util.Random;

public class AdventureOutcomeReward implements AdventureOutcome {
	private final String id;
	private final float moneyMean;
	private final float moneyStd;
	private final ItemList items;
	private final String region;
	private final PlayerView view;

	public AdventureOutcomeReward(String id, float moneyMean, float moneyStd, String region, ItemList items) {
		this.id = id;
		this.moneyMean = moneyMean;
		this.moneyStd = moneyStd;
		this.region = region;
		this.items = items;
		this.view = new AdventureOutcomeViewReward(id, region);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void apply(Player player, Random r) {
		Random random = new Random();
		Inventory backpack = player.getData().getBackpack();

		long money = Math.max(0, (long)random.nextGaussian(moneyMean, moneyStd));
		backpack.addMoney(money);
		player.getStats().setLastIncome(money);

		items.add(backpack);

		player.getData().setRegion(region);
	}

	@Override
	public PlayerView getView() {
		return view;
	}
}
