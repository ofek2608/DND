package com.ofek2608.dnd.data;

import com.ofek2608.dnd.data.adventure.AdventureWorld;
import com.ofek2608.dnd.data.item.Items;
import com.ofek2608.dnd.data.lang.Languages;
import com.ofek2608.dnd.resources.GameRegistry;
import com.ofek2608.dnd.resources.IGameData;
import com.ofek2608.dnd.resources.LoadContext;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;
import com.ofek2608.gim.GimSerializer;
import com.ofek2608.utils.ResourcesUtils;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.util.Objects;

public final class Data {
	private Data() {}
	public static void loadClass() {}



	private static final String PACKAGES_PATH = "packages";
	public static final GameRegistry REGISTRY = new GameRegistry();

	public static final Languages LANGUAGES;
	public static final AdventureWorld ADVENTURE;
	public static final Items ITEMS;

	static {
		System.out.println("[Data] Start reading");

		Gim readData = readData();

		System.out.println("[Data] Finished reading");


		System.out.println("[Data] Start loading");

		LoadContext ctx = new LoadContext(REGISTRY, readData);
		LANGUAGES = ctx.get("lang").load(Languages.class);
		ADVENTURE = ctx.get("adventure").load(AdventureWorld.class);
		ITEMS = ctx.get("item").load(Items.class);

		System.out.println("[Data] Finished loading");
	}

	private static Gim readData() {
		GimBuilder builder = new GimBuilder();
		for (String packName : ResourcesUtils.getString(PACKAGES_PATH).split("\n"))
			GimSerializer.fromResources(ResourcesUtils::getString, builder, PACKAGES_PATH + "/" + packName);
		return builder.build();
	}

	@Contract("null->null")
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T extends IGameData> T get(@Nullable String path) {
		return (T) REGISTRY.get(path);
	}

	public static <T extends IGameData> T getNonnull(String path) {
		return Objects.requireNonNull(get(path));
	}
}
