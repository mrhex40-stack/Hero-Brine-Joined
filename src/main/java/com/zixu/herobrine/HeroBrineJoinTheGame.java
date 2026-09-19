package com.zixu.herobrine;

import com.zixu.herobrine.entity.ModEntities;
import com.zixu.herobrine.event.HerobrineEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HeroBrineJoinTheGame.MODID)
public class HeroBrineJoinTheGame {
    public static final String MODID = "herobrine_join";

    public HeroBrineJoinTheGame() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
        MinecraftForge.EVENT_BUS.register(new HerobrineEvents());
    }
}
