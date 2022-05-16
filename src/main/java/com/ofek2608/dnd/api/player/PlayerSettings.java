package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Lang;
import com.ofek2608.dnd.api.Savable;

public interface PlayerSettings extends Savable {
	void setLanguage(Lang language);
	Lang getLanguage();
}
