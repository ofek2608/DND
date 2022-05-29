package com.ofek2608.dnd.data.lang;

import com.ofek2608.dnd.resources.LoadableGameData;
import com.ofek2608.dnd.resources.LoadContext;

import javax.annotation.Nullable;
import java.util.Map;

public final class Languages extends LoadableGameData {
	private static final String DEFAULT_LANG = "en_us";
	@Res(mapKind = Lang.class) private final Map<String, Lang> languages = fieldNonnull();
	public final Lang defLang = languages.get(DEFAULT_LANG);

	public Languages(LoadContext ctx) {
		super(ctx);
	}

	@Nullable
	public Lang get(String name) {
		return languages.get(name);
	}
}
