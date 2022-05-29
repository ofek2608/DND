package com.ofek2608.dnd.impl;

import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.impl.bot.DNDBot;
import com.ofek2608.dnd.impl.view.GameViews;

public class DNDMain {
	public static void main(String[] args) {
		run();
	}

	private static void run() {
		Data.loadClass();
		GameViews.loadClass();
		DNDBot.loadClass();
	}
}
