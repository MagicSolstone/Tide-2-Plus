package com.li64.tide.data.rods;

import com.li64.tide.Tide;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Where glow hooks have put their light, saved with the world. A hook takes its light away when it moves or is
 * reeled in; if the server stops mid-cast (a crash), whatever is still listed is taken away when it starts again.
 */
public final class GlowLights extends SavedData {
    public static final int LEVEL = 8;
    private static final Codec<GlowLights> CODEC = Codec.STRING.listOf().xmap(GlowLights::new, g -> new ArrayList<>(g.spots));
    public static final SavedDataType<GlowLights> TYPE = new SavedDataType<>(Tide.resource("glow_hook_lights"), GlowLights::new, CODEC,
            DataFixTypes.SAVED_DATA_COMMAND_STORAGE);

    private final Set<String> spots = new LinkedHashSet<>();

    public GlowLights() {}

    private GlowLights(List<String> spots) {
        this.spots.addAll(spots);
    }

    private static GlowLights of(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    private static String key(ServerLevel level, BlockPos pos) {
        return level.dimension().identifier() + "|" + pos.asLong();
    }

    public static void placed(ServerLevel level, BlockPos pos) {
        GlowLights g = of(level);
        if (g.spots.add(key(level, pos))) g.setDirty();
    }

    public static void removed(ServerLevel level, BlockPos pos) {
        GlowLights g = of(level);
        if (g.spots.remove(key(level, pos))) g.setDirty();
    }

    /** Puts the water (or air) back where a glow hook's light was left. */
    public static void restore(ServerLevel level, BlockPos pos) {
        BlockState there = level.getBlockState(pos);
        if (there.is(Blocks.LIGHT) && there.getValue(LightBlock.LEVEL) == LEVEL)
            level.setBlock(pos, there.getValue(LightBlock.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
    }

    /** On server start: no hooks are out yet, so any listed light is a leftover. */
    public static void cleanUp(MinecraftServer server) {
        GlowLights g = server.overworld().getDataStorage().computeIfAbsent(TYPE);
        if (g.spots.isEmpty()) return;
        int n = 0;
        for (String spot : g.spots) {
            String[] parts = spot.split("\\|");
            ServerLevel level = server.getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.parse(parts[0])));
            if (level == null) continue;
            restore(level, BlockPos.of(Long.parseLong(parts[1])));
            n++;
        }
        g.spots.clear();
        g.setDirty();
        Tide.LOG.info("Took away {} glow hook light(s) left from before the last restart", n);
    }
}
