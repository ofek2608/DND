package com.ofek2608.dnd.api;

import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;

public interface Savable {
	void save(GimBuilder builder);
	void load(Gim data);
}
