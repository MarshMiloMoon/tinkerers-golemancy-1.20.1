package net.starglobe.tinkergolems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.starglobe.tinkergolems.entity.ModEntities;
import net.starglobe.tinkergolems.entity.client.BasicGolemModel;
import net.starglobe.tinkergolems.entity.client.BasicGolemRenderer;
import net.starglobe.tinkergolems.entity.client.ModModelLayers;

public class TinkerersGolemancyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.BASIC_GOLEM, BasicGolemRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BASIC_GOLEM, BasicGolemModel::getTexturedModelData);
    }
}
