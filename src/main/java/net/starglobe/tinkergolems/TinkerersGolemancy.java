package net.starglobe.tinkergolems;

import net.fabricmc.api.ModInitializer;

import net.starglobe.tinkergolems.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinkerersGolemancy implements ModInitializer {
	public static final String MOD_ID = "tinkergolems";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
	}
}