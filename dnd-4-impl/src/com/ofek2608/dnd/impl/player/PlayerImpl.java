package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.*;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.data.adventure.AdventureEvent;
import com.ofek2608.dnd.data.adventure.AdventureOutcome;
import com.ofek2608.dnd.data.adventure.AdventureRegion;
import com.ofek2608.dnd.impl.view.GameViews;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PlayerImpl implements Player {
	private static final Map<Long, Player> MAP = new HashMap<>();

	public static Player getPlayer(User user) {
		return MAP.computeIfAbsent(user.getIdLong(), id->loadPlayer(user));
	}

	private static Player loadPlayer(User user) {
		Player player = new PlayerImpl(user);
		player.load(Gim.EMPTY);//TODO load data from file if exist
		return player;
	}

	private final User user;
	private final PlayerSettingsImpl settings;
	private final PlayerMessage message;
	private final PlayerData data;
	private final PlayerStats stats;

	public PlayerImpl(User user) {
		this.user = user;
		this.settings = new PlayerSettingsImpl();
		this.message = new PlayerMessageImpl(this);
		this.data = new PlayerDataImpl(this);
		this.stats = new PlayerStatsImpl();
	}

	@Override
	public void save(GimBuilder builder) {
		settings.save(builder.child("settings"));
		message .save(builder.child("message" ));
		data    .save(builder.child("data"    ));
		stats   .save(builder.child("stats"   ));
	}

	@Override
	public void load(Gim data) {
		this.settings.load(data.get("settings"));
		this.message .load(data.get("message" ));
		this.data    .load(data.get("data"    ));
		this.stats   .load(data.get("stats"   ));
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public PlayerSettingsImpl getSettings() {
		return settings;
	}

	@Override
	public PlayerMessage getMessage() {
		return message;
	}

	@Override
	public PlayerData getData() {
		return data;
	}

	@Override
	public PlayerStats getStats() {
		return stats;
	}






	@Override
	public void openMenu() {
		message.setView(GameViews.MENU);
	}

	@Override
	public void openAdventure() {
		@Nullable
		String regionName = data.getRegion();

		if (regionName != null && Data.get(regionName + ".view") instanceof PlayerView view)
			message.setView(view);
		else
			message.setView(GameViews.HOME);
	}

	@Override
	public void openInventory() {
		message.setView(GameViews.INVENTORY);
	}

	@Override
	public void openPets() {
		message.setView(GameViews.PETS);
	}

	@Override
	public void continueAdventure(Random r) {
		@Nullable
		String regionId = data.getRegion();
		if (regionId == null) {
			message.setView(GameViews.HOME);
			return;
		}
		if (!(Data.get(regionId) instanceof AdventureRegion region)) {
			goHome();
			return;
		}
		AdventureEvent event = region.eventWeights.get(r);
		if (!(Data.get(event.path + ".view") instanceof PlayerView view)) {
			goHome();
			return;
		}
		message.setView(view);
	}

	@Override
	public void goHome() {
		data.getInventory().addAll(data.getBackpack());
		respawn();
	}

	@Override
	public void respawn() {
		data.setHealth(1);
		data.setRegion(null);
		data.getBackpack().clear();
		message.setView(GameViews.HOME);
	}






	@Override
	public void runAdventureOutcome(AdventureOutcome outcome, Random r) {
		if (outcome instanceof AdventureOutcome.Die) {
			data.kill();
			return;
		}
		if (outcome instanceof AdventureOutcome.Reward reward) {
			Inventory backpack = data.getBackpack();

			long money = Math.max(0, (long)r.nextGaussian(reward.moneyMean, reward.moneyStd));
			backpack.addMoney(money);
			stats.setLastIncome(money);

			backpack.addItems(reward.items);

			if (reward.region != null)
				data.setRegion(reward.region);
			return;
		}
		throw new IllegalArgumentException();//should not happen
	}
}
