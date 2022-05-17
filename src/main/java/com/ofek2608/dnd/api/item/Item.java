package com.ofek2608.dnd.api.item;

import com.ofek2608.dnd.api.Identifiable;
import com.ofek2608.dnd.api.Named;
import com.ofek2608.dnd.api.Roll;

import javax.annotation.Nullable;

public interface Item extends Identifiable, Named {
	String getCategory();
	String getIcon();
	@Nullable
	Roll getAttack();
	float getProtection();
	float getHeal();
}
