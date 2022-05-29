package com.ofek2608.dnd.impl.player;

import com.ofek2608.dnd.api.player.PlayerSettings;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.data.lang.Lang;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;

public class PlayerSettingsImpl implements PlayerSettings {
	private Lang language = Data.LANGUAGES.defLang;

	@Override
	public void save(GimBuilder builder) {
		builder.child("language").set(language.path);
	}

	@Override
	public void load(Gim data) {
		language = Data.get(data.get("language").valueString) instanceof Lang lang ? lang : Data.LANGUAGES.defLang;
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
