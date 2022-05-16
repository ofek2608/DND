package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.PlayerViewable;
import com.ofek2608.dnd.api.adventure.AdventureRegion;
import com.ofek2608.dnd.impl.adventure.AdventureHomeView;
import com.ofek2608.dnd.resources.Resources;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;
import java.util.*;

public final class PlayerImpl implements Player {

	private static final Map<Long, Player> MAP = new HashMap<>();

	public static Player getPlayer(User user) {
		return MAP.computeIfAbsent(user.getIdLong(), id->loadPlayer(user));
	}

	private static Player loadPlayer(User user) {
		//TODO load data from file if exist
		return new PlayerImpl(user);
	}

	private final User user;
	@Nullable private TextChannel channel;
	@Nullable private Message message;
	private Lang lang = (Lang) Resources.get("lang.en_us");
	private PlayerViewable view = (PlayerViewable) Resources.get("view.menu");
	private String viewData = "";
	private float health;
	private long money;
	private long backpackMoney;
	private long lastDeath;
	private final Map<String,Integer> items = new HashMap<>();
	private final Map<String,Integer> backpack = new HashMap<>();
	@Nullable private String region;
	private long lastIncome;

	private PlayerImpl(User user) {
		this.user = user;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public @Nullable TextChannel getChannel() {
		return channel;
	}

	@Override
	public @Nullable Message getMessage() {
		return message;
	}

	@Override
	public void setLanguage(Lang lang) {
		this.lang = lang;
	}

	@Override
	public Lang getLanguage() {
		return lang;
	}

	@Override
	public PlayerViewable getView() {
		return view;
	}

	@Override
	public void setView(PlayerViewable view) {
		this.view = view;
		updateView();
	}

	@Override
	public void updateView() {
		if (message == null)
			return;

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setAuthor(user.getName(), null, user.getAvatarUrl());
		view.buildEmbed(this, embedBuilder);

		message.editMessage(new MessageBuilder()
				.setContent(" ")
				.setEmbeds(embedBuilder.build())
				.setActionRows(view.createActionRows(this))
				.build()
		).queue(m->this.message = m);
	}

	@Override
	public String getViewData() {
		return viewData;
	}

	@Override
	public void setViewData(String viewData) {
		this.viewData = viewData;
	}

	@Override
	public void sendMessage(MessageChannel channel) {
		if (this.message != null) {
			this.message.delete().queue();
			this.message = null;
		}
		channel.sendMessage("<@" + user.getId() + ">").queue(message->{
			this.message = message;
			updateView();
		});
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public void damage(float value) {
		float newHealth = health - value;//TODO multiply by defence
		if (newHealth < 0) {
			newHealth = 0;
			kill();
		}
		health = newHealth;
	}

	@Override
	public void kill() {
		lastDeath = System.currentTimeMillis();
		health = 1;
		region = null;
		backpack.clear();
		//TODO send private message?
	}

	@Override
	public long getLastDeath() {
		return lastDeath;
	}

	@Override
	public void goHome() {
		health = 1;
		region = null;
		unloadBackpack();
		setView(AdventureHomeView.INSTANCE);
	}

	@Override
	public void respawn() {
		health = 1;
		region = null;
		clearBackpack();
		setView(AdventureHomeView.INSTANCE);
	}

	@Override
	public long getMoney() {
		return money;
	}

	@Override
	public void setMoney(long money) {
		this.money = money;
	}

	@Override
	public long getBackpackMoney() {
		return backpackMoney;
	}

	@Override
	public void setBackpackMoney(long backpackMoney) {
		this.backpackMoney = backpackMoney;
	}

	@Override
	public String[] getItems() {
		Set<String> items = new HashSet<>();
		items.addAll(this.items.keySet());
		items.addAll(this.backpack.keySet());
		return items.toArray(new String[0]);
	}

	@Override
	public int getItemCount(String item) {
		return items.getOrDefault(item, 0);
	}

	@Override
	public void setItemCount(String item, int count) {
		if (count <= 0)
			items.remove(item);
		else
			items.put(item, count);
	}

	@Override
	public int getBackpackItemCount(String item) {
		return backpack.getOrDefault(item, 0);
	}

	@Override
	public void setBackpackItemCount(String item, int count) {
		if (count <= 0)
			backpack.remove(item);
		else
			backpack.put(item, count);
	}

	@Override
	public void unloadBackpack() {
		money += backpackMoney;
		backpackMoney = 0;

		backpack.forEach((item,count) -> items.put(item, items.getOrDefault(item, 0) + count));
		backpack.clear();
	}

	@Override
	public void clearBackpack() {
		backpack.clear();
		backpackMoney = 0;
	}

	@Override
	public void setRegion(@Nullable String region) {
		this.region = region;
	}

	@Nullable
	@Override
	public String getRegion() {
		return region;
	}

	@Override
	public void continueAdventure() {
		if (this.region == null) {
			setView(AdventureHomeView.INSTANCE);
			return;
		}
		Identifiable region = Resources.get(this.region);
		if (!(region instanceof AdventureRegion)) {
			setRegion(null);
			setView(AdventureHomeView.INSTANCE);
			return;
		}
		setView(((AdventureRegion)region).getEvent(new Random()));
	}

	@Override
	public void setLastIncome(long lastIncome) {
		this.lastIncome = lastIncome;
	}

	@Override
	public long getLastIncome() {
		return lastIncome;
	}

	@Override
	public void openInventory() {
		viewData = "";
		setView(InventoryView.INSTANCE);
	}
}
