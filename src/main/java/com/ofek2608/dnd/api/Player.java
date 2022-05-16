package com.ofek2608.dnd.api;

import com.ofek2608.dnd.api.item.Item;
import com.ofek2608.dnd.impl.PlayerImpl;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.Nullable;

public interface Player {
	int REVIVE_TIME = 5000;//21600000

	static Player getPlayer(User user) {
		return PlayerImpl.getPlayer(user);
	}



	User getUser();
	@Nullable TextChannel getChannel();
	@Nullable Message getMessage();

	void setLanguage(Lang lang);
	Lang getLanguage();

	PlayerViewable getView();
	void setView(PlayerViewable view);
	void updateView();

	String getViewData();
	void setViewData(String data);

	void sendMessage(MessageChannel channel);


	float getHealth();

	void damage(float value);

	void kill();

	long getLastDeath();

	default long getReviveTime() {
		return getLastDeath() + REVIVE_TIME;
	}

	default boolean isAlive() {
		return getReviveTime() <= System.currentTimeMillis();
	}

	void goHome();
	void respawn();





	long getMoney();

	void setMoney(long money);

	long getBackpackMoney();

	void setBackpackMoney(long money);

	default void addMoney(long add) {
		if (add == 0)
			return;
		if (add > 0) {
			setBackpackMoney(getBackpackMoney() + add);
			return;
		}
		long count = getBackpackMoney() + add;
		if (count >= 0)
			setBackpackMoney(count);
		else {
			setBackpackMoney(0);
			setMoney(getMoney() + count);
		}
	}



	default int getItemCount(Item item) {
		return getItemCount(item.getId());
	}

	default void setItemCount(Item item, int count) {
		setItemCount(item.getId(), count);
	}

	default int getBackpackItemCount(Item item) {
		return getBackpackItemCount(item.getId());
	}

	default void setBackpackItemCount(Item item, int count) {
		setBackpackItemCount(item.getId(), count);
	}

	default int getTotalItemCount(Item item) {
		return getTotalItemCount(item.getId());
	}

	default void addItem(Item item, int by) {
		addItem(item.getId(), by);
	}


	String[] getItems();

	int getItemCount(String item);

	void setItemCount(String item, int count);

	int getBackpackItemCount(String item);

	void setBackpackItemCount(String item, int count);

	default int getTotalItemCount(String item) {
		return getItemCount(item) + getBackpackItemCount(item);
	}

	default void addItem(String item, int by) {
		if (by == 0)
			return;
		if (by > 0) {
			setBackpackItemCount(item, getBackpackItemCount(item) + by);
			return;
		}
		int count = getBackpackItemCount(item) + by;
		if (count >= 0)
			setBackpackItemCount(item, count);
		else {
			setBackpackItemCount(item, 0);
			setItemCount(item, getItemCount(item) + count);
		}
	}

	void unloadBackpack();
	void clearBackpack();



	void setRegion(@Nullable String region);
	@Nullable String getRegion();

	void continueAdventure();

	void setLastIncome(long lastIncome);
	long getLastIncome();

	void openInventory();
}
