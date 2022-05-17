package com.ofek2608.dnd.bot;

import com.ofek2608.dnd.api.player.PlayerMessage;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public final class DNDBotListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		User author = event.getAuthor();
		if (author.isBot())
			return;

		if (!(event.getChannel() instanceof TextChannel channel))
			return;//must be inside a guild

		String msg = event.getMessage().getContentRaw();
		msg = msg.replaceAll("^ *", "");

		String botId = DNDBot.BOT.getSelfUser().getId();

		if (msg.startsWith("<@" + botId + ">"))
			msg = msg.substring(botId.length() + 3);
		else if (msg.startsWith("<@&" + botId + ">"))
			msg = msg.substring(botId.length() + 4);
		else
			return;
		msg = msg.replaceAll("^ *| *$", "");
		System.out.println("/" + msg);

		Player.getPlayer(author).getMessage().sendMessage(channel);
	}


	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		User user = event.getUser();
		Player player = Player.getPlayer(user);
		PlayerMessage playerMessage = player.getMessage();

		if (playerMessage.getMessageId() != event.getMessage().getIdLong())
			return;

		event.deferEdit().queue();

		String id = event.getButton().getId();
		if (id != null)
			playerMessage.getView().onClick(new PlayerView.Context(player), id);
	}

	@Override
	public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
		User user = event.getUser();
		Player player = Player.getPlayer(user);
		PlayerMessage playerMessage = player.getMessage();

		if (playerMessage.getMessageId() != event.getMessage().getIdLong())
			return;

		event.deferEdit().queue();

		String id = event.getComponent().getId();
		if (id != null)
			playerMessage.getView().onSelection(new PlayerView.Context(player), id, event.getValues());
	}
}
