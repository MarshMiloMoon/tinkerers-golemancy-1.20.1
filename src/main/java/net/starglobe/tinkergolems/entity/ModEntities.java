package net.starglobe.tinkergolems.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.starglobe.tinkergolems.TinkerersGolemancy;
import net.starglobe.tinkergolems.entity.custom.BasicGolemEntity;

public class ModEntities {
    public static final EntityType<BasicGolemEntity> BASIC_GOLEM = Registry.register(Registries.ENTITY_TYPE, 
            new Identifier(TinkerersGolemancy.MOD_ID, "basic_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BasicGolemEntity::new)
                    .dimensions(EntityDimensions.fixed(1, 1)).build());
}
