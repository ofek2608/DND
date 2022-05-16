package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.PlayerViewable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nullable;

public interface PlayerMessage {
	@Nullable TextChannel getChannel();
	@Nullable Message getMessage();
	void sendMessage(MessageChannel channel);
	PlayerViewable getView();
	void setView(PlayerViewable view);
	String getViewData();
	void setViewData(String data);
	void updateView();
}
