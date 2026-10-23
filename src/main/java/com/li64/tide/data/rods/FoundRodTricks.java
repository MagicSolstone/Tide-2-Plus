package com.li64.tide.data.rods;

import com.li64.tide.Tide;
import com.li64.tide.registries.TideItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/** The sickle and hex rods' tricks when a fish is landed. */
public final class FoundRodTricks {
    private FoundRodTricks() {}

    private static final int REACH = 8, HEIGHT = 3, MAX_CROPS = 4;

    public static void onCatch(ServerPlayer player, ItemStack rod, RandomSource random) {
        if (rod.is(TideItems.SICKLE_FISHING_ROD) && (Tide.CI || random.nextDouble() < RodEffects.SICKLE_GROW)) growCrops(player, random);
        if (rod.is(TideItems.HEX_FISHING_ROD) && (Tide.CI || random.nextDouble() < RodEffects.HEX_EFFECT)) hex(player, random);
    }

    /** Like a pinch of bone meal on a few crops near the angler (only crops: no flowers or grass sprouting on lawns). */
    private static void growCrops(ServerPlayer player, RandomSource random) {
        ServerLevel level = player.level();
        BlockPos at = player.blockPosition();
        List<BlockPos> crops = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(at.offset(-REACH, -HEIGHT, -REACH), at.offset(REACH, HEIGHT, REACH))) {
            BlockState state = level.getBlockState(pos);
            if (state.is(BlockTags.CROPS) && state.getBlock() instanceof BonemealableBlock b && b.isValidBonemealTarget(level, pos, state, net.minecraft.world.level.block.BonemealSource.INTERACTION))
                crops.add(pos.immutable());
        }
        int grown = 0;
        for (int i = 0; i < MAX_CROPS && !crops.isEmpty(); i++) {
            BlockPos pos = crops.remove(random.nextInt(crops.size()));
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof BonemealableBlock b) {
                b.performBonemeal(level, random, pos, state, net.minecraft.world.level.block.BonemealSource.INTERACTION);
                grown++;
                level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.3, 0.3, 0.3, 0);
            }
        }
        if (grown > 0) com.li64.tide.data.angler.Angler.markEffect(player, "Sickle rod: " + grown + (grown == 1 ? " crop grew" : " crops grew"));
        if (Tide.CI) Tide.LOG.info("TIDE-CI sickle: {} crops grew", grown);
    }

    private record Hex(Holder<MobEffect> effect, int ticks, String name) {}

    private static final List<Hex> HEXES = List.of(
            new Hex(MobEffects.NIGHT_VISION, 1200, "Night Vision"), new Hex(MobEffects.WATER_BREATHING, 1200, "Water Breathing"),
            new Hex(MobEffects.LUCK, 1200, "Luck"), new Hex(MobEffects.SPEED, 1200, "Speed"),
            new Hex(MobEffects.FIRE_RESISTANCE, 1200, "Fire Resistance"), new Hex(MobEffects.DOLPHINS_GRACE, 1200, "Dolphin's Grace"),
            new Hex(MobEffects.REGENERATION, 300, "Regeneration"));

    private static void hex(ServerPlayer player, RandomSource random) {
        Hex h = HEXES.get(random.nextInt(HEXES.size()));
        player.addEffect(new MobEffectInstance(h.effect(), h.ticks(), 0));
        com.li64.tide.data.angler.Angler.markEffect(player, "Hex rod: " + h.name() + " (" + h.ticks() / 20 + "s)");
        player.level().sendParticles(ParticleTypes.WITCH, player.getX(), player.getY() + 1, player.getZ(), 10, 0.4, 0.5, 0.4, 0);
    }
}
