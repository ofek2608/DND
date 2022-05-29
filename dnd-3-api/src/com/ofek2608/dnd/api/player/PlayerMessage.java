package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nullable;

public interface PlayerMessage extends Savable {
	@Nullable TextChannel getChannel();
	long getMessageId();
	void sendMessage(TextChannel channel);

	PlayerView getView();
	void setView(PlayerView view, String data);
	String getViewData();
	void setViewData(String data);
	void setViewDataNoUpdate(String data);
	void updateView();

	default void setView(PlayerView view) {
		setView(view, "");
	}

	void privateMessage(Message message);

	default void privateMessage(String title, String description, int color) {
		MessageEmbed embed = new EmbedBuilder().setTitle(title).appendDescription(description).setColor(color).build();
		Message message = new MessageBuilder().setEmbeds(embed).build();
		privateMessage(message);
	}
}
