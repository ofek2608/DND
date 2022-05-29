package com.ofek2608.dnd.impl.view;

import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.pet.PlayerPet;
import com.ofek2608.dnd.api.player.pet.PlayerPets;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.data.pet.PetKind;
import com.ofek2608.dnd.data.pet.Trap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public final class PetView extends AbstractPlayerView {
	private final int index;
	private final PetDiscardView discard;

	public PetView(int index) {
		super("view.pet." + (index < 0 ? "overview" : index));
		this.index = index;
		this.discard = new PetDiscardView(this, index);
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		//TODO
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		ActionRow selection = createSelectionActionRow(context);
		if (index < 0)
			return new ActionRow[] {selection};

		PlayerPet pet = context.player.getData().getPets().get(index);

		@Nullable
		PetKind kind = pet.getKind();
		if (kind == null)
			return new ActionRow[] {selection};

		Button discard = Button.danger("discard", context.t("button.pet.discard"));

		if (pet.getTrap() != null) {
			Button open = Button.primary("open", context.t("button.pet.trap.open")).withDisabled(pet.captureTime() >= 0);
			return new ActionRow[] { ActionRow.of(open, discard), selection };
		}

		@Nullable
		String selectedFood = getSelectedFood(context, kind);

		ActionRow foodSelect = createFoodSelectionActionRow(context, kind, selectedFood);
		ActionRow actions = ActionRow.of(
				Button.primary(context.t("button.pet.feed"), "feed").withDisabled(selectedFood == null),
				Button.secondary(context.t("button.pet.rename"), "rename"),
				discard
		);

		if (!pet.isTame())
			return new ActionRow[] { foodSelect, actions, selection };

		ActionRow commands = createCommandsActionRow(context);

		return new ActionRow[] { commands, foodSelect, actions, selection };
	}

	private ActionRow createSelectionActionRow(Context context) {
		SelectMenu.Builder builder = SelectMenu.create("pet");

		builder.addOption(context.t("button.pet.overview"), "view.pet.overview");


		PlayerPets pets = context.player.getData().getPets();

		IntStream.range(0, PlayerPets.PET_COUNT).mapToObj(pets::get).sorted((pet0,pet1)->{
			if (pet0.getKind() == null)
				return pet1.getKind() == null ? 0 : 1;
			if (pet1.getKind() == null)
				return -1;
			if (pet0.getTrap() != null)
				return pet1.getTrap() != null ? 0 : 1;
			if (pet1.getTrap() != null)
				return -1;
			return pet0.getName().compareTo(pet1.getName());
		}).forEach(pet->{
			int index = pet.getIndex();
			@Nullable PetKind kind = pet.getKind();
			@Nullable Trap trap = pet.getTrap();
			if (kind == null)
				builder.addOption(context.t("button.pet.empty"), "view.pet." + index);
			else if (trap != null)
				builder.addOption(context.t("button.pet.trap"), "view.pet." + index, pet.captureTime() < 0 ?
						context.t("pet.trap.trapped").replaceAll("%p", context.t(kind.path)) :
						context.t("pet.trap.waiting")
				);
			else
				builder.addOption(pet.getName(), "view.pet." + index, context.t(kind.path));
		});

		builder.setDefaultValues(Collections.singletonList(path));

		return ActionRow.of(builder.build());
	}

	private ActionRow createCommandsActionRow(Context context) {
		PlayerPets pets = context.player.getData().getPets();
		PlayerPet pet = pets.get(index);

		boolean isGuard = pets.getGuardIndex() == index;
		boolean isAssist = pets.getAssistIndex() == index;
		boolean isHunt = pet.getNextHunt() >= 0;
		boolean isRetire = !(isGuard || isAssist || isHunt);

		Button guardBtn  = Button.of(isGuard  ? ButtonStyle.SUCCESS : ButtonStyle.SECONDARY, "guard" , context.t("button.pet.command.guard" )).withDisabled(isGuard );
		Button assistBtn = Button.of(isAssist ? ButtonStyle.SUCCESS : ButtonStyle.SECONDARY, "assist", context.t("button.pet.command.assist")).withDisabled(isAssist);
		Button huntBtn   = Button.of(isHunt   ? ButtonStyle.SUCCESS : ButtonStyle.SECONDARY, "hunt"  , context.t("button.pet.command.hunt"  )).withDisabled(isHunt  );
		Button retireBtn = Button.of(isRetire ? ButtonStyle.SUCCESS : ButtonStyle.SECONDARY, "retire", context.t("button.pet.command.retire")).withDisabled(isRetire);

		return ActionRow.of(guardBtn, assistBtn, huntBtn, retireBtn);
	}

	private @Nullable String getSelectedFood(Context context, PetKind kind) {
		String data = context.player.getMessage().getViewData();
		if (data.length() == 0)
			return null;
		if (context.player.getData().getBothInventories().getItem(data) == 0)
			return null;
		if (!kind.food.containsKey(data))
			return null;
		return data;
	}

	private ActionRow createFoodSelectionActionRow(Context context, PetKind kind, @Nullable String selectedFood) {
		SelectMenu.Builder builder = SelectMenu.create("food");
		Inventory inventory = context.player.getData().getBothInventories();
		kind.food.keySet().stream()
				.filter(item->inventory.getItem(item) > 0)
				.sorted()
				.forEach(item-> builder.addOption(context.t(item), item));
		String data = context.player.getMessage().getViewData();
		if (data.length() > 0)
			builder.setDefaultValues(Collections.singletonList(data));
		return ActionRow.of(builder.build());
	}

	@Override
	public void onClick(Context context, String id) {
		if (index < 0)
			return;

		PlayerPets pets = context.player.getData().getPets();
		PlayerPet pet = pets.get(index);

		pet.update();

		switch (id) {
			case "discard" -> context.player.getMessage().setView(discard);

			case "open" -> pet.openTrap();//TODO what if time just passed?

			case "guard" -> pet.guard();
			case "assist" -> pet.assist();
			case "hunt" -> pet.hunt();
			case "retire" -> pet.retire();

			case "feed" -> {
				@Nullable PetKind kind = pet.getKind();
				@Nullable String food = kind == null ? null : getSelectedFood(context, kind);
				if (food != null) pet.feed(food);
			}

			case "rename" -> {
				System.out.println("rename");
				System.out.println(pet.getName());
				//TODO
			}

		}
	}

	@Override
	public void onSelection(Context context, String id, List<String> values) {
		if (values.size() == 0)
			return;
		String value = values.get(0);

		switch (id) {
			case "pet" -> {
				if (Data.get(value) instanceof PetView view)
					context.player.getMessage().setView(view);
			}
			case "food" -> context.player.getMessage().setViewData(value);
		}
	}
}
