package com.ofek2608.dnd.api.adventure;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.player.PlayerView;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.util.Random;

public interface AdventureAction extends Identifiable {
	Cost getCost();
	Cost getTicket();
	@Nullable Button createButton(PlayerView.Context context);
	AdventureOutcome getOutcome(Random r);
}
