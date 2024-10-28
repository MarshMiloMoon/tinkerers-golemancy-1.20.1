package net.starglobe.tinkergolems;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.starglobe.tinkergolems.entity.ModEntities;
import net.starglobe.tinkergolems.entity.custom.BasicGolemEntity;
import net.starglobe.tinkergolems.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinkerersGolemancy implements ModInitializer {
	public static final String MOD_ID = "tinkergolems";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

		FabricDefaultAttributeRegistry.register(ModEntities.BASIC_GOLEM, BasicGolemEntity.createBasicGolemAttributes());
	}
}