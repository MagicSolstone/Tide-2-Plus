package com.li64.tide.data.rods;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Chummed water: when a hook baited with chum settles, the water around it is chummed for a couple of minutes, and
 * every hook in it (anyone's) bites faster. Kept in memory only: a restart clears it, which is fine for two minutes.
 */
public final class ChumZones {
    private ChumZones() {}

    public static final int RADIUS = 12;
    public static final long DURATION = 2400;          // ticks (2 minutes)
    public static final double BITE_BONUS = 0.25;      // +25% bite speed inside

    private record Zone(ResourceKey<Level> dimension, BlockPos pos, long until) {}

    private static final List<Zone> ZONES = new ArrayList<>();

    /** Chum the water at {@code pos} (an existing zone close by is refreshed, not doubled). Returns true for a new zone. */
    public static synchronized boolean add(ServerLevel level, BlockPos pos) {
        long now = level.getGameTime();
        ZONES.removeIf(z -> z.until() < now);
        boolean fresh = ZONES.stream().noneMatch(z -> z.dimension() == level.dimension() && z.pos().closerThan(pos, 4));
        ZONES.removeIf(z -> z.dimension() == level.dimension() && z.pos().closerThan(pos, 4));
        ZONES.add(new Zone(level.dimension(), pos.immutable(), now + DURATION));
        level.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 40, 1.5, 0.3, 1.5, 0.05);
        level.sendParticles(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 30, 1.5, 0.1, 1.5, 0.1);
        return fresh;
    }

    /** Multiply a bite wait by this at {@code pos}: faster inside chummed water, unchanged outside. */
    public static synchronized double waitFactor(ServerLevel level, BlockPos pos) {
        long now = level.getGameTime();
        for (Zone z : ZONES)
            if (z.until() >= now && z.dimension() == level.dimension() && z.pos().closerThan(pos, RADIUS)) return 1.0 / (1.0 + BITE_BONUS);
        return 1.0;
    }
}
