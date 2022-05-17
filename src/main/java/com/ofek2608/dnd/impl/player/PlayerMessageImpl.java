package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.PlayerMessage;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.bot.DNDBot;
import com.ofek2608.dnd.resources.Resources;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerMessageImpl implements PlayerMessage {
	private final Player player;
	@Nullable private TextChannel channel;
	private long messageId;
	private PlayerView view = Resources.getDefaultView();
	private String viewData = "";

	public PlayerMessageImpl(Player player) {
		this.player = player;

	}

	@Override
	public Object saveJson() {
		Map<String,Object> result = new HashMap<>();
		if (channel != null) {
			result.put("guildId", channel.getGuild().getIdLong());
			result.put("channelId", channel.getIdLong());
		}
		result.put("messageId", messageId);
		result.put("view", view.getId());
		result.put("viewData", viewData);
		return result;
	}

	@Override
	public void loadJson(@Nullable Object json) {
		Map<?,?> jsonMap = json instanceof Map<?,?> m ? m : Collections.EMPTY_MAP;
		if (jsonMap.get("guildId") instanceof Number guildId && jsonMap.get("channelId") instanceof Number channelId) {
			@Nullable
			Guild guild = DNDBot.BOT.getGuildById(guildId.longValue());
			if (guild != null)
				this.channel = guild.getTextChannelById(channelId.longValue());
		}
		this.messageId = jsonMap.get("messageId") instanceof Number n ? n.longValue() : 0;
		this.view = jsonMap.get("view") instanceof String s && Resources.get(s) instanceof PlayerView view ?
				view :
				Resources.getDefaultView();
		this.viewData = jsonMap.get("viewData") instanceof String s ? s : "";
	}

	@Nullable
	@Override
	public TextChannel getChannel() {
		return channel;
	}

	@Override
	public long getMessageId() {
		return messageId;
	}

	@Override
	public void sendMessage(TextChannel channel) {
		if (this.channel != null) {
			this.channel.deleteMessageById(this.messageId).queue();
			this.channel = null;
		}
		channel.sendMessage("<@" + player.getUser().getId() + ">").queue(message->{
			this.messageId = message.getIdLong();
			this.channel = channel;
			updateView();
		});
	}

	@Override
	public PlayerView getView() {
		return view;
	}

	@Override
	public void setView(PlayerView view, String data) {
		this.view = view;
		this.viewData = data;
		updateView();
	}

	@Override
	public String getViewData() {
		return viewData;
	}

	@Override
	public void setViewData(String data) {
		this.viewData = data;
		updateView();
	}

	@Override
	public void setViewDataNoUpdate(String data) {
		this.viewData = data;
	}

	@Override
	public void updateView() {
		if (channel == null)
			return;

		PlayerView.Context context = new PlayerView.Context(player);

		User user = player.getUser();

		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setAuthor(user.getName(), null, user.getAvatarUrl());
		view.buildEmbed(context, embedBuilder);


		channel.editMessageById(messageId, new MessageBuilder()
				.setContent(" ")
				.setEmbeds(embedBuilder.build())
				.setActionRows(view.createActionRows(context))
				.build()
		).queue();
	}
}
