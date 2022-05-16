package com.ofek2608.dnd.bot;

import com.ofek2608.dnd.api.Player;
import net.dv8tion.jda.api.entities.Message;
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

		Player player = Player.getPlayer(author);
		player.sendMessage(event.getChannel());
	}


	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		User user = event.getUser();
		Player player = Player.getPlayer(user);
		Message message = event.getMessage();

		if (!message.equals(player.getMessage()))
			return;

		event.deferEdit().queue();

		String id = event.getButton().getId();
		if (id != null)
			player.getView().onClick(player, id);
	}

	@Override
	public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
		User user = event.getUser();
		Player player = Player.getPlayer(user);
		Message message = event.getMessage();

		if (!message.equals(player.getMessage()))
			return;

		event.deferEdit().queue();

		String id = event.getComponent().getId();
		if (id != null)
			player.getView().onSelection(player, id, event.getValues());
	}
}
