package com.zixu.herobrine.event;

import com.zixu.herobrine.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class HerobrineEvents {
    private final Map<UUID, Integer> joinTimers = new HashMap<>();
    private final Map<UUID, Integer> encounterCooldown = new HashMap<>();
    private final Set<UUID> firstJoinDone = new HashSet<>();
    private final Random random = new Random();

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer p)) return;
        UUID id = p.getUUID();

        // Per-session timer. Persistent first-join state is stored using a persistent tag.
        if (!p.getPersistentData().getBoolean("hero_brine_first_join_done")) {
            joinTimers.put(id, 100); // 5 seconds at 20 TPS
        } else {
            encounterCooldown.put(id, 200);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.player.level().isClientSide) return;
        if (!(e.player instanceof ServerPlayer p)) return;
        UUID id = p.getUUID();

        if (joinTimers.containsKey(id)) {
            int left = joinTimers.get(id) - 1;
            if (left <= 0) {
                firstJoin(p);
                joinTimers.remove(id);
                p.getPersistentData().putBoolean("hero_brine_first_join_done", true);
            } else joinTimers.put(id, left);
            return;
        }

        int cd = encounterCooldown.getOrDefault(id, 300);
        if (cd > 0) {
            encounterCooldown.put(id, cd - 1);
            return;
        }

        // New random encounter, then a long random cooldown.
        if (random.nextInt(1200) == 0) {
            doRandomEncounter(p);
            encounterCooldown.put(id, 1200 + random.nextInt(2400));
        }
    }

    private void firstJoin(ServerPlayer p) {
        p.sendSystemMessage(Component.literal("Herobrine joined the game"));
        clearNearbyLeaves(p.serverLevel(), p.blockPosition(), 10);
        p.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, false, false));
    }

    private void doRandomEncounter(ServerPlayer p) {
        switch (random.nextInt(4)) {
            case 0 -> distantSight(p);
            case 1 -> fogScare(p);
            case 2 -> blockScare(p);
            default -> attackScare(p);
        }
    }

    private void distantSight(ServerPlayer p) {
        ServerLevel level = p.serverLevel();
        BlockPos pos = p.blockPosition().offset(
                (random.nextBoolean() ? 1 : -1) * (12 + random.nextInt(8)),
                random.nextInt(3),
                (random.nextBoolean() ? 1 : -1) * (12 + random.nextInt(8)));
        var h = ModEntities.HEROBRINE.get().create(level);
        if (h != null) {
            h.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5, random.nextFloat() * 360, 0);
            h.setNoAi(true);
            level.addFreshEntity(h);
            // 2 seconds later the server-side entity is removed by its own lifetime marker.
            h.getPersistentData().putInt("scare_life", 40);
        }
        p.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 30, 0, false, false));
    }

    private void fogScare(ServerPlayer p) {
        p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 35, 0, false, false));
        p.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 55, 0, false, false));
        p.sendSystemMessage(Component.literal("..."));
    }

    private void blockScare(ServerPlayer p) {
        BlockPos base = p.blockPosition().above();
        if (p.serverLevel().isEmptyBlock(base)) {
            p.serverLevel().setBlock(base, Blocks.SOUL_TORCH.defaultBlockState(), 3);
        }
        p.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 25, 0, false, false));
    }

    private void attackScare(ServerPlayer p) {
        ServerLevel level = p.serverLevel();
        BlockPos pos = p.blockPosition().relative(p.getDirection().getOpposite(), 3);
        var h = ModEntities.HEROBRINE.get().create(level);
        if (h != null) {
            h.moveTo(pos.getX() + .5, pos.getY(), pos.getZ() + .5, p.getYRot(), 0);
            level.addFreshEntity(h);
            p.hurt(level.damageSources().mobAttack(h), 6.0f);
            h.discard();
            p.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 0, false, false));
        }
    }

    private void clearNearbyLeaves(ServerLevel level, BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius))) {
            BlockState s = level.getBlockState(pos);
            if (s.is(Blocks.OAK_LEAVES) || s.is(Blocks.BIRCH_LEAVES) ||
                s.is(Blocks.SPRUCE_LEAVES) || s.is(Blocks.JUNGLE_LEAVES) ||
                s.is(Blocks.ACACIA_LEAVES) || s.is(Blocks.DARK_OAK_LEAVES) ||
                s.is(Blocks.MANGROVE_LEAVES) || s.is(Blocks.CHERRY_LEAVES)) {
                level.destroyBlock(pos, false);
            }
        }
    }
}
