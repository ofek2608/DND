package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.api.item.Item;
import com.ofek2608.dnd.api.player.Inventory;
import com.ofek2608.dnd.api.player.PlayerMessage;
import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.resources.Resources;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class InventoryView implements PlayerView {
	public static final InventoryView INSTANCE = new InventoryView();
	private InventoryView() {}

	@Override
	public String getId() {
		return "view.inventory";
	}

	@Override
	public void buildEmbed(Context context, EmbedBuilder builder) {
		Player player = context.player;
		PlayerMessage playerMessage = player.getMessage();

		PlayerStats stats = new PlayerStats(player);

		if (stats.categories.length == 0) {
			builder.appendDescription(context.t("description.inventory.empty"));
			playerMessage.setViewDataNoUpdate("");
			return;
		}

		@Nullable
		Data data = Data.fromString(playerMessage.getViewData());
		if (data == null)
			data = new Data(stats.categories[0], 0, false);

		if (data.page < 0)
			data.page = 0;

		int categoryIndex = data.getCategoryOfStats(stats);
		Item[] items = stats.itemsByCategory[categoryIndex];


		Inventory playerInv = player.getData().getInventory();
		Inventory playerBack = player.getData().getBackpack();

		if (data.detailed) {
			data.page = Math.min(data.page, items.length - 1);

			Item item = items[data.page];
			int count = playerInv.getItem(item);
			int backpackCount = playerBack.getItem(item);

			builder.appendDescription(item.getIcon() + " " + context.t(item.getId()) + ": " + count);
			if (backpackCount > 0)
				builder.appendDescription(" [(+" + backpackCount + ")](https://www.google.com)");
			builder.appendDescription("\n");

			builder.appendDescription(context.t(item.getId() + ".description"));
		} else {
			data.page = Math.min(data.page, (items.length + 9) / 10);
			int off = data.page * 10;
			for (int i = 0; i < 10; i++) {
				if (off + i >= items.length)
					break;

				Item item = items[off + i];
				int count = playerInv.getItem(item);
				int backpackCount = playerBack.getItem(item);

				builder.appendDescription(item.getIcon() + " " + context.t(item.getId()) + ": " + count);
				if (backpackCount > 0)
					builder.appendDescription(" [(+" + backpackCount + ")](https://www.google.com)");
				builder.appendDescription("\n");
			}
		}

		playerMessage.setViewDataNoUpdate(data.toString());
	}

	@Override
	public ActionRow[] createActionRows(Context context) {
		ActionRow menuActionRow = ActionRow.of(Button.secondary("menu", context.t("button.back_to_menu")));

		//when this function is called, buildEmbed had already been called, so no need to worry about illegal data
		@Nullable
		Data data = Data.fromString(context.player.getMessage().getViewData());
		if (data == null)
			return new ActionRow[] {menuActionRow};

		PlayerStats stats = new PlayerStats(context.player);
		int categoryIndex = data.getCategoryOfStats(stats);
		Item[] items = stats.itemsByCategory[categoryIndex];

		int maxPage = data.detailed ? items.length : (items.length + 9) / 10;

		ActionRow pageActionRow = ActionRow.of(
				Button.secondary("first", "<<<").withDisabled(data.page <= 0),
				Button.secondary("previous", "<").withDisabled(data.page <= 0),
				Button.secondary("change_view", (data.page + 1) + "/" + maxPage),
				Button.secondary("next", ">").withDisabled(data.page >= maxPage - 1),
				Button.secondary("last", ">>>").withDisabled(data.page >= maxPage - 1)
		);

		ActionRow categoryActionRow;
		{
			SelectMenu.Builder builder = SelectMenu.create("category");
			for (String category : stats.categories)
				builder.addOption(context.t(category), category, context.t(category + ".description"));
			builder.setDefaultOptions(Collections.singletonList(builder.getOptions().get(categoryIndex)));
			categoryActionRow = ActionRow.of(builder.build());
		}
		if (!data.detailed) {
			return new ActionRow[]{
					pageActionRow,
					categoryActionRow,
					menuActionRow
			};
		}

		return new ActionRow[]{
				pageActionRow,
				categoryActionRow,
				ActionRow.of(//TODO fix, this is just example
						Button.secondary("equip", "Equip"),
						Button.secondary("eat", "Eat")
				),
				menuActionRow
		};
	}

	@Override
	public void onClick(Context context, String id) {
		if (id.equals("menu")) {
			context.player.openMenu();
			return;
		}

		PlayerMessage playerMessage = context.player.getMessage();
		//when this function is called, buildEmbed had already been called, so no need to worry about illegal data
		@Nullable
		Data data = Data.fromString(playerMessage.getViewData());
		if (data == null)
			return;

		switch (id) {
			case "first"    -> data.page = 0;
			case "previous" -> data.page--;
			case "next"     -> data.page++;
			case "last"     -> data.page = Integer.MAX_VALUE;
			case "change_view" -> {
				if (data.detailed) {
					data.detailed = false;
					data.page /= 10;
				} else {
					data.detailed = true;
					data.page *= 10;
				}
			}
			default -> {
				return;
			}
		}
		playerMessage.setViewData(data.toString());
	}

	@Override
	public void onSelection(Context context, String id, List<String> values) {
		if (values.size() == 0)
			return;
		PlayerMessage playerMessage = context.player.getMessage();
		@Nullable
		Data data = Data.fromString(playerMessage.getViewData());
		if (data == null)
			return;
		data.category = values.get(0);
		playerMessage.setViewData(data.toString());
	}

	private static class Data {
		private String category;
		private int page;
		private boolean detailed;

		public Data(String category, int page, boolean detailed) {
			this.category = category;
			this.page = page;
			this.detailed = detailed;
		}

		@Override
		public String toString() {
			return category + ";" + page + ";" + (detailed ? 't' : 'f');
		}

		@Nullable
		public static Data fromString(String str) {
			String[] split = str.split(";");
			if (split.length != 3)
				return null;
			try {
				return new Data(split[0], Integer.parseInt(split[1]), split[2].equals("t"));
			} catch (NumberFormatException ignored) {
				return null;
			}
		}


		public int getCategoryOfStats(PlayerStats stats) {
			for (int i = 0; i < stats.categories.length; i++)
				if (category.equals(stats.categories[i]))
					return i;
			category = stats.categories[0];
			page = 0;
			return 0;
		}
	}

	private static class PlayerStats {
		private final String[] categories;
		private final Item[][] itemsByCategory;

		public PlayerStats(Player player) {
			Item[] items = player
					.getData()
					.getBothInventories()
					.getItems()
					.stream()
					.sorted()
					.map(Resources::get)
					.map(o->o instanceof Item i ? i : null)
					.filter(Objects::nonNull)
					.toArray(Item[]::new);

			String[] categories = Stream.of(items)
					.map(Item::getCategory)
					.sorted()
					.distinct()
					.toArray(String[]::new);

			this.categories = categories;

			this.itemsByCategory = Stream.of(categories)
					.map(category->Stream.of(items)
							.filter(item -> item.getCategory().equals(category))
							.toArray(Item[]::new)
					)
					.toArray(Item[][]::new);
		}
	}
}
