package com.ofek2608.dnd.impl.view;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public final class PetDiscardView extends AbstractPlayerView {
	private final PetView petView;
	private final int index;

	public PetDiscardView(PetView petView, int index) {
		super(petView.path + ".discard");
		this.petView = petView;
		this.index = index;
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		//TODO
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		return new ActionRow[] {ActionRow.of(
				Button.danger("discard", context.t("button.pet.discard")),
				Button.secondary("cancel", context.t("button.cancel"))
		)};
	}

	@Override
	public void onClick(Context context, String id) {
		if (id.equals("discard"))
			context.player.getData().getPets().get(index).discard();
		context.player.getMessage().setView(petView);
	}
}
