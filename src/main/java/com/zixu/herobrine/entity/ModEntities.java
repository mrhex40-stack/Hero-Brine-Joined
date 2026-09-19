package com.zixu.herobrine.entity;

import com.zixu.herobrine.HeroBrineJoinTheGame;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HeroBrineJoinTheGame.MODID);

    public static final RegistryObject<EntityType<HerobrineEntity>> HEROBRINE =
            ENTITIES.register("herobrine",
                    () -> EntityType.Builder.of(HerobrineEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(32)
                            .build("herobrine"));
}
