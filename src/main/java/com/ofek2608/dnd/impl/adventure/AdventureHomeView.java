package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.api.PlayerViewable;
import com.ofek2608.dnd.impl.MenuView;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.util.Random;

public class AdventureHomeView implements PlayerViewable {
	public static final AdventureHomeView INSTANCE = new AdventureHomeView();
	private AdventureHomeView() {}

	@Override
	public String getId() {
		return "view.adventure_home";
	}

	@Override
	public void buildEmbed(Player player, EmbedBuilder builder) {
		Random r = new Random();
		builder.appendDescription(player.getLanguage().get(
				player.isAlive() ?
						"description.adventure.home.alive" :
						"description.adventure.home.dead",
				r
		).replaceAll("%t", "<t:" + player.getReviveTime() / 1000 + ":R>"));
	}

	@Override
	public ActionRow[] createActionRows(Player player) {
		Random r = new Random();
		Lang lang = player.getLanguage();

		if (player.isAlive()) {
			return new ActionRow[] {
					ActionRow.of(
							Button.secondary("forest", lang.get("region.forest", r)),
							Button.secondary("gifts", lang.get("region.gifts", r)),
							Button.secondary("forest2", lang.get("region.forest", r)),
							Button.secondary("forest3", lang.get("region.forest", r))
					),
					ActionRow.of(Button.secondary("menu", lang.get("button.back_to_menu", r)))
			};
		} else {
			return new ActionRow[] {
					ActionRow.of(Button.secondary("menu", lang.get("button.back_to_menu", r)))
			};
		}
	}

	@Override
	public void onClick(Player player, String id) {
		if (id.equals("menu")) {
			player.setView(MenuView.INSTANCE);
			return;
		}
		if (!player.isAlive())
			return;
		@Nullable
		String regionId = switch (id) {
			case "forest"->"region.forest";
			case "gifts"->"region.gifts";
			default -> null;
		};
		if (regionId == null)
			return;
		player.setRegion(regionId);
		player.continueAdventure();
	}
}
