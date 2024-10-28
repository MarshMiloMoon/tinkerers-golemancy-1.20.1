package net.starglobe.tinkergolems.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.starglobe.tinkergolems.TinkerersGolemancy;
import net.starglobe.tinkergolems.entity.custom.BasicGolemEntity;

public class BasicGolemRenderer extends MobEntityRenderer<BasicGolemEntity, BasicGolemModel<BasicGolemEntity>> {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/blocks/copper.png");

    public BasicGolemRenderer(EntityRendererFactory.Context context) {
        super(context, new BasicGolemModel<>(context.getPart(ModModelLayers.BASIC_GOLEM)), 0.75f);
    }

    @Override
    public Identifier getTexture(BasicGolemEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(BasicGolemEntity mobEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {

        if(mobEntity.hasHat()) {
            matrixStack.scale(1.125f, 1.125f, 1.125f);
        } else {
            matrixStack.scale(1, 1, 1);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
