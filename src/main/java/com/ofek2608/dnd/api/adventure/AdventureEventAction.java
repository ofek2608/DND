package com.ofek2608.dnd.api.adventure;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.Player;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.util.Random;

public interface AdventureEventAction extends Identifiable {
	Cost getCost();
	@Nullable Button createButton(Player player, Random r);
	AdventureEventOutcome getOutcome(Random r);
}
