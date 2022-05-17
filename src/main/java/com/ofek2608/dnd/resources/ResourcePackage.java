package com.ofek2608.dnd.resources;

import com.ofek2608.dnd.api.Cost;
import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.adventure.AdventureEvent;
import com.ofek2608.dnd.api.adventure.AdventureAction;
import com.ofek2608.dnd.api.adventure.AdventureOutcome;
import com.ofek2608.dnd.api.adventure.AdventureRegion;
import com.ofek2608.dnd.api.item.Item;
import com.ofek2608.dnd.api.item.ItemCategory;
import com.ofek2608.dnd.api.Roll;
import com.ofek2608.dnd.impl.CostImpl;
import com.ofek2608.dnd.impl.item.ItemCategoryImpl;
import com.ofek2608.dnd.impl.item.ItemImpl;
import com.ofek2608.dnd.impl.ItemList;
import com.ofek2608.dnd.impl.item.RollImpl;
import com.ofek2608.dnd.impl.adventure.*;
import com.ofek2608.dnd.utils.ArraysUtils;
import com.ofek2608.dnd.utils.Weight;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import javax.annotation.Nullable;
import java.util.*;

public final class ResourcePackage {
	private static final String PATH = "packages";
	private static final String PATH_LANGUAGES = "lang";
	private static final String PATH_REGIONS = "region";
	private static final String REGION_PROPERTIES = ".properties.json";
	private static final String PATH_ITEMS = "item";
	private static final String PATH_ITEM_CATEGORIES = "item_category";

	public static ResourcePackage[] loadAllPackages() {
		String[] packagesNames = ResourcesReader.getString(PATH).split("\n");
		List<ResourcePackage> packages = new ArrayList<>();

		for (String name : packagesNames) {
			try {
				packages.add(parsePackage(name));
			} catch (PackageParsingException e) {
				System.err.println("Couldn't load package '" + name + "'\n" + e.getMessage());
			}
		}

		return packages.toArray(new ResourcePackage[0]);
	}

	public static ResourcePackage parsePackage(String name) throws PackageParsingException {
		ResourcePackage resourcePackage = new ResourcePackage(name);
		resourcePackage.parse();
		return resourcePackage;
	}


	public final String name;
	public final String path;
	final Map<String, Identifiable> identifiables = new HashMap<>();
	final Map<String, Map<String,Object>> languagesMap = new HashMap<>();


	private ResourcePackage(String name) {
		this.name = name;
		this.path = PATH + "/" + name + "/";
	}

	private void parse() throws PackageParsingException {
		parseLanguages();
		parseRegions();
		parseItems();
		parseItemCategories();
	}

	private void register(Identifiable ... identifiables) {
		for (Identifiable identifiable : identifiables)
			this.identifiables.put(identifiable.getId(), identifiable);
	}


	//==================
	// Language Parsing
	//==================


	private void parseLanguages() throws PackageParsingException {
		String[] languages = ResourcesReader.getString(path + PATH_LANGUAGES).split("\n");
		for (String lang : languages)
			parseLanguage(lang);
	}

	private void parseLanguage(String lang) throws PackageParsingException {
		Object json = ResourcesReader.getJson(path + PATH_LANGUAGES + "/" + lang);

		int dot = lang.lastIndexOf('.');
		if (dot >= 0)
			lang = lang.substring(0, dot);

		Map<String,Object> langMap = languagesMap.computeIfAbsent(lang, l->new HashMap<>());
		loadLangToMap(lang, langMap, json, "");
	}

	private static void loadLangToMap(String lang, Map<String, Object> langMap, Object json, String prefix)
			throws PackageParsingException {
		if (json instanceof List<?> list) {
			Weight<String> weight = parseLangWeights(list);
			if (weight == null)
				throw new PackageParsingException("IllegalList; In language '" + lang + "' at '" + prefix + "'");
			langMap.put(prefix, weight);
			return;
		}

		if (json instanceof Map<?,?> map) {
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String newPrefix = getLangChildPrefix(prefix, Objects.toString(entry.getKey()));
				loadLangToMap(lang, langMap, entry.getValue(), newPrefix);
			}
			return;
		}

		langMap.put(prefix, Objects.toString(json));
	}

	private static String getLangChildPrefix(String prefix, String key) {
		//Simplify key
		key = key.toLowerCase();
		key = key.replaceAll("[^a-z0-9._]", "");
		key = key.replaceAll("\\.+", ".");
		key = key.replaceAll("^\\.|\\.$", "");
		//combine
		return prefix.length() == 0 ? key : key.length() == 0 ? prefix : prefix + "." + key;
	}

	@Nullable
	private static Weight<String> parseLangWeights(List<?> list) {
		int size = list.size();
		if ((size & 1) == 1)
			return null;

		int halfSize = size >> 1;

		float[] weights = new float[halfSize];
		String[] options = new String[halfSize];

		for (int i = 0; i < halfSize; i++) {
			Object weight = list.get(i << 1);
			Object option = list.get(i << 1 | 1);

			if (!(weight instanceof Number) || !(option instanceof String))
				return null;

			weights[i] = ((Number)weight).floatValue();
			options[i] = (String)option;
		}

		return new Weight<>(options, weights);
	}



	//================
	// Region Parsing
	//================

	private void parseRegions() throws PackageParsingException {
		String[] regions = ResourcesReader.getString(path + PATH_REGIONS).split("\n");
		for (String region : regions)
			parseRegion(region);
	}

	private void parseRegion(String region) throws PackageParsingException {
		String regionPath = path + PATH_REGIONS + "/" + region;

		Object regionPropertiesJson = ResourcesReader.getJson(regionPath + "/" + REGION_PROPERTIES);
		Map<?,?> regionProperties = regionPropertiesJson instanceof Map<?,?> m ? m : Collections.EMPTY_MAP;
		boolean escapable = regionProperties.get("escapable") instanceof Boolean b ? b : true;

		List<Float> weights = new ArrayList<>();
		List<AdventureEvent> events = new ArrayList<>();

		String[] eventsNames = ResourcesReader.getString(regionPath).split("\n");
		for (String eventName : eventsNames) {
			if (eventName.equals(REGION_PROPERTIES))
				continue;

			Object eventJson = ResourcesReader.getJson(regionPath + "/" + eventName);

			int dot = eventName.lastIndexOf('.');
			if (dot >= 0)
				eventName = eventName.substring(0, dot);


			if (!(eventJson instanceof Map<?,?>))
				throw new PackageParsingException("IllegalEvent; In region '" + region + "' at '" + eventName + "'");

			AdventureEvent event = parseEvent(region, eventName, (Map<?,?>)eventJson);
			if (event == null)
				throw new PackageParsingException("IllegalEvent; In region '" + region + "' at '" + eventName + "'");

			events.add(event);

			Object weight = ((Map<?,?>)eventJson).get("weight");
			weights.add(weight instanceof Number ? ((Number)weight).floatValue() : 1);
		}

		Weight<AdventureEvent> eventsWeights = new Weight<>(
				events.toArray(new AdventureEvent[0]),
				ArraysUtils.toArray(weights)
		);

		AdventureRegion parsed = new AdventureRegionImpl(region, escapable, eventsWeights);
		register(parsed, parsed.getView());
	}

	@Nullable
	private AdventureEvent parseEvent(String region, String eventName, Map<?,?> eventJson) {
		if (!(eventJson.get("actions") instanceof List<?> eventJsonList))
			return null;

		String id = "region." + region + "." + eventName;

		int index = 0;
		int rowCount = eventJsonList.size();
		AdventureAction[][] actionsTable = new AdventureAction[rowCount][];
		for (int row = 0; row < rowCount; row++) {
			Object rowJson = eventJsonList.get(row);
			if (!(rowJson instanceof List<?> rowJsonList))
				return null;

			int colCount = rowJsonList.size();
			AdventureAction[] rowActions = new AdventureAction[colCount];
			for (int col = 0; col < colCount; col++) {
				AdventureAction action = parseEventAction(id + "." + index, region, rowJsonList.get(col));
				index++;
				if (action == null)
					return null;
				rowActions[col] = action;
			}


			actionsTable[row] = rowActions;
		}

		AdventureEvent event = new AdventureEventImpl(id, actionsTable);
		register(event, event.getView());
		return event;
	}

	private AdventureAction parseEventAction(String id, String inRegion, Object json) {
		if (!(json instanceof Map<?,?> jsonMap))
			return null;

		boolean hidden = jsonMap.get("hidden") instanceof Boolean b ? b : false;
		Cost cost = parseCost(jsonMap.get("cost"));
		Cost ticket = parseCost(jsonMap.get("ticket"));
		ButtonStyle style = parseButtonStyle(jsonMap.get("style"));

		Object outcomesJson = jsonMap.get("outcome");
		Weight<AdventureOutcome> outcomesWeights;

		if (outcomesJson instanceof Map<?,?> outcomesJsonMap) {
			AdventureOutcome outcome = parseEventOutcome(id + ".0", inRegion, outcomesJsonMap);
			outcomesWeights = new Weight<>(new AdventureOutcome[] {outcome}, new float[]{1});
		} else if (outcomesJson instanceof List<?> outcomesJsonList) {
			int size = outcomesJsonList.size();
			AdventureOutcome[] outcomes = new AdventureOutcome[size];
			float[] weights = new float[size];

			for (int i = 0; i < size; i++) {
				Object singleOutcomeJson = outcomesJsonList.get(i);
				if (!(singleOutcomeJson instanceof Map<?, ?> singleOutcomeJsonMap))
					return null;
				outcomes[i] = parseEventOutcome(id + "." + i, inRegion, singleOutcomeJsonMap);
				//weird warning
				//noinspection ConstantConditions
				weights[i] = singleOutcomeJsonMap.get("weight") instanceof Number n ? n.floatValue() : 1;
			}


			outcomesWeights = new Weight<>(outcomes, weights);
		} else {
			return null;
		}






		AdventureAction action = new AdventureActionImpl(id, hidden, cost, ticket, style, outcomesWeights);
		register(action);
		return action;
	}

	private AdventureOutcome parseEventOutcome(String id, String inRegion, Map<?, ?> json) {
		float   moneyMean = json.get("money"   ) instanceof Number  n ? n.floatValue() : 0.0f;
		float   moneyStd  = json.get("moneyStd") instanceof Number  n ? n.floatValue() : 0.0f;
		boolean die       = json.get("die"     ) instanceof Boolean b ? b              : false;
		String  region    = json.get("region"  ) instanceof String  s ? s              : inRegion;
		ItemList items = ItemList.parseItemList(json.get("items"));
		if (items == null) items = ItemList.EMPTY;

		AdventureOutcome outcome;
		if (die) {
			outcome = new AdventureOutcomeDie(id);
		} else {
			outcome = new AdventureOutcomeReward(id, moneyMean, moneyStd, "region." + region, items);
		}

		register(outcome, outcome.getView());

		return outcome;
	}

	private static Cost parseCost(Object cost) {
		if (!(cost instanceof Map<?,?> costMap))
			return Cost.EMPTY;

		long money = costMap.get("money") instanceof Number n ? Math.max(n.longValue(),0) : 0;
		ItemList items = ItemList.parseItemList(costMap.get("items"));

		if (items == null)
			items = ItemList.EMPTY;

		return new CostImpl(money, items);
	}

	private static ButtonStyle parseButtonStyle(Object style) {
		return switch (Objects.toString(style).toUpperCase()) {
			case "PRIMARY" -> ButtonStyle.PRIMARY;
			default        -> ButtonStyle.SECONDARY;
			case "SUCCESS" -> ButtonStyle.SUCCESS;
			case "DANGER"  -> ButtonStyle.DANGER;
		};
	}



	//==============
	// Item Parsing
	//==============

	private void parseItems() throws PackageParsingException {
		String[] items = ResourcesReader.getString(path + PATH_ITEMS).split("\n");
		for (String itemName : items) {
			Object json = ResourcesReader.getJson(path + PATH_ITEMS + "/" + itemName);

			int dot = itemName.lastIndexOf('.');
			if (dot >= 0)
				itemName = itemName.substring(0, dot);

			Item item = parseItem(itemName, json);
			if (item == null)
				throw new PackageParsingException("IllegalItem; at '" + itemName + "'");
			register(item);
		}
	}

	@Nullable
	private static Item parseItem(String name, Object json) {
		if (!(json instanceof Map<?,?> map))
			return null;

		if (!(map.get("category") instanceof String category)) return null;
		if (!(map.get("icon") instanceof String icon)) return null;
		Roll attack = RollImpl.parseString(map.get("attack"));
		float protection = map.get("protection") instanceof Number n ? n.floatValue() : 0;
		float heal = map.get("heal") instanceof Number n ? n.floatValue() : 0;


		return new ItemImpl(name, "item_category." + category, icon, attack, protection, heal);
	}



	//=========================
	// Item Categories Parsing
	//=========================

	private void parseItemCategories() throws PackageParsingException {
		String[] items = ResourcesReader.getString(path + PATH_ITEM_CATEGORIES).split("\n");
		for (String categoryName : items) {
			Object json = ResourcesReader.getJson(path + PATH_ITEM_CATEGORIES + "/" + categoryName);

			int dot = categoryName.lastIndexOf('.');
			if (dot >= 0)
				categoryName = categoryName.substring(0, dot);

			ItemCategory category = parseItemCategory(categoryName, json);
			if (category == null)
				throw new PackageParsingException("IllegalItemCategory; at '" + categoryName + "'");
			register(category);
		}
	}

	@Nullable
	private static ItemCategory parseItemCategory(String name, Object json) {
		if (!(json instanceof Map<?,?> map))
			return null;

		if (!(map.get("icon") instanceof String icon)) return null;


		return new ItemCategoryImpl(name, icon);
	}
}