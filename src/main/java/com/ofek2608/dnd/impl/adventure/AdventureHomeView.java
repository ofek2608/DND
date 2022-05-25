package com.ofek2608.dnd.impl.adventure;

import com.ofek2608.dnd.api.player.PlayerData;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;

public class AdventureHomeView implements PlayerView {
	public static final AdventureHomeView INSTANCE = new AdventureHomeView();
	private AdventureHomeView() {}

	@Override
	public String getId() {
		return "view.adventure_home";
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		PlayerData data = context.player.getData();
		builder.appendDescription(context.t(
				data.isAlive() ?
						"description.adventure.home.alive" :
						"description.adventure.home.dead"
		).replaceAll("%t", "<t:" + data.getReviveTime() / 1000 + ":R>"));
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		if (context.player.getData().isAlive()) {
			return new ActionRow[] {
					ActionRow.of(
							Button.secondary("forest", context.t("region.forest")),
							Button.secondary("gifts", context.t("region.gifts")),
							Button.secondary("forest2", context.t("region.forest")),
							Button.secondary("forest3", context.t("region.forest"))
					),
					ActionRow.of(Button.secondary("menu", context.t("button.back_to_menu")))
			};
		} else {
			return new ActionRow[] {
					ActionRow.of(Button.secondary("menu", context.t("button.back_to_menu")))
			};
		}
	}

	@Override
	public void onClick(Context context, String id) {
		Player player = context.player;
		if (id.equals("menu")) {
			player.openMenu();
			return;
		}
		if (!player.getData().isAlive())
			return;
		@Nullable
		String regionId = switch (id) {
			case "forest"->"region.forest";
			case "gifts"->"region.gifts";
			default -> null;
		};
		if (regionId == null)
			return;
		player.getData().setRegion(regionId);
		player.continueAdventure();
	}
}
