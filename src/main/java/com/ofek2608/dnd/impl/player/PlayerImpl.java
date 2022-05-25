package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.adventure.AdventureEvent;
import com.ofek2608.dnd.api.adventure.AdventureRegion;
import com.ofek2608.dnd.api.player.PlayerData;
import com.ofek2608.dnd.api.player.PlayerMessage;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerStats;
import com.ofek2608.dnd.impl.InventoryView;
import com.ofek2608.dnd.impl.MenuView;
import com.ofek2608.dnd.impl.adventure.AdventureHomeView;
import com.ofek2608.dnd.resources.Resources;
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
		player.loadJson(null);//TODO load data from file if exist
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
	public Object saveJson() {
		return Map.of(
				"settings", settings.saveJson(),
				"message" , message .saveJson(),
				"data"    , data    .saveJson(),
				"stats"   , stats   .saveJson()
		);
	}

	@Override
	public void loadJson(@Nullable Object json) {
		if (!(json instanceof Map<?,?> jsonMap))
			return;
		settings.loadJson(jsonMap.get("settings"));
		message .loadJson(jsonMap.get("message" ));
		data    .loadJson(jsonMap.get("data"    ));
		stats   .loadJson(jsonMap.get("stats"   ));
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
		message.setView(MenuView.INSTANCE);
	}

	@Override
	public void openAdventure() {
		@Nullable
		String regionName = data.getRegion();

		if (regionName != null && Resources.get(regionName) instanceof AdventureRegion region)
			message.setView(region.getView());
		else
			message.setView(AdventureHomeView.INSTANCE);
	}

	@Override
	public void openInventory() {
		message.setView(InventoryView.INSTANCE);
	}

	@Override
	public void continueAdventure() {
		@Nullable
		String regionId = data.getRegion();
		if (regionId == null) {
			message.setView(AdventureHomeView.INSTANCE);
			return;
		}
		if (!(Resources.get(regionId) instanceof AdventureRegion region)) {
			goHome();
			return;
		}
		AdventureEvent event = region.getEvent(new Random());
		message.setView(event.getView());
	}

	@Override
	public void goHome() {
		data.getInventory().addAll(data.getBackpack());
		respawn();
	}

	@Override
	public void respawn() {
		data.getHealth().setHealth(1);
		data.setRegion(null);
		data.getBackpack().clear();
		message.setView(AdventureHomeView.INSTANCE);
	}
}
