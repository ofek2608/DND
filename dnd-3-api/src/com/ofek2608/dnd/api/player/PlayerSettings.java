package com.ofek2608.dnd.api.player;

import com.ofek2608.dnd.api.Savable;
import com.ofek2608.dnd.data.lang.Lang;

public interface PlayerSettings extends Savable {
	void setLanguage(Lang language);
	Lang getLanguage();
}
