package net.starglobe.tinkergolems.entity.client;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.starglobe.tinkergolems.TinkerersGolemancy;

public class ModModelLayers {
    public static final EntityModelLayer BASIC_GOLEM =
            new EntityModelLayer(new Identifier(TinkerersGolemancy.MOD_ID, "basic_golem"), "main");
}
