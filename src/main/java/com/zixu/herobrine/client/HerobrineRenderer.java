package com.zixu.herobrine.client;

import com.zixu.herobrine.entity.HerobrineEntity;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class HerobrineRenderer extends MobRenderer<HerobrineEntity, ZombieModel<HerobrineEntity>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("herobrine_join", "textures/entity/herobrine.png");

    public HerobrineRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ZombieModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(HerobrineEntity e) {
        return TEXTURE;
    }
}
