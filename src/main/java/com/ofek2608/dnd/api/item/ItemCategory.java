package com.ofek2608.dnd.api.item;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.Named;

public interface ItemCategory extends Identifiable, Named {
	String getIcon();
}
