package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerMessage;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.impl.bot.DNDBot;
import com.ofek2608.dnd.impl.view.GameViews;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

public class PlayerMessageImpl implements PlayerMessage {
	private final Player player;
	@Nullable private TextChannel channel;
	private long messageId;
	private PlayerView view = GameViews.MENU;
	private String viewData = "";

	public PlayerMessageImpl(Player player) {
		this.player = player;
	}

	@Override
	public void save(GimBuilder builder) {
		if (channel != null) {
			builder.child("guildId").set(channel.getGuild().getIdLong());
			builder.child("channelId").set(channel.getIdLong());
		}
		builder.child("messageId").set(messageId);
		builder.child("view").set(view.getPath());
		builder.child("viewData").set(viewData);
	}

	@Override
	public void load(Gim data) {
		@Nullable Number guildId = data.get("guildId").valueNumber;
		@Nullable Number channelId = data.get("channelId").valueNumber;
		if (guildId != null && channelId != null) {
			@Nullable
			Guild guild = DNDBot.BOT.getGuildById(guildId.longValue());
			if (guild != null)
				this.channel = guild.getTextChannelById(channelId.longValue());
		}

		this.messageId = data.get("messageId").getLong();
		this.view = Data.get(data.get("view").valueString) instanceof PlayerView view ? view : GameViews.MENU;
		this.viewData = data.get("viewData").getString("");
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

	@Override
	public void privateMessage(Message message) {
		player.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
	}
}
