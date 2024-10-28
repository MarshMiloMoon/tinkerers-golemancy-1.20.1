package net.starglobe.tinkergolems.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.starglobe.tinkergolems.TinkerersGolemancy;

public class ModItems {
    public static final Item WRENCH = registerItem("wrench", new WrenchItem(new FabricItemSettings()));

    private static void addItemsToToolTab(FabricItemGroupEntries entries) {
        entries.add(WRENCH);
    }
    private static void addItemsToCombatTab(FabricItemGroupEntries entries) {
        entries.add(WRENCH);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TinkerersGolemancy.MOD_ID, name), item);
    }

    public static void  registerModItems() {
        TinkerersGolemancy.LOGGER.info("Registering Mod Items for " + TinkerersGolemancy.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolTab);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToCombatTab);
    }
}
