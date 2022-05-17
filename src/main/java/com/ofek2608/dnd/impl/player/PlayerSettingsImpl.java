package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.player.PlayerSettings;
import com.ofek2608.dnd.resources.Resources;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlayerSettingsImpl implements PlayerSettings {
	private Lang language = Resources.getDefaultLanguage();

	@Override
	public Object saveJson() {
		return Map.of(
				"language", language.getId()
		);
	}

	@Override
	public void loadJson(@Nullable Object json) {
		if (!(json instanceof Map<?,?> jsonMap))
			return;
		language = jsonMap.get("language") instanceof String langId && Resources.get(langId) instanceof Lang lang ?
				lang :
				Resources.getDefaultLanguage();

	}

	@Override
	public void setLanguage(Lang language) {
		this.language = language;
	}

	@Override
	public Lang getLanguage() {
		return language;
	}
}
