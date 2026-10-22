package com.li64.tide.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Small helpers for things Minecraft 26.x does differently from 1.21.1. */
public final class Mc26 {
    private Mc26() {}

    /** The moon phase (0 = full moon .. 7), as Level.getMoonPhase() used to give. */
    public static int moonPhase(Level level, Vec3 pos) {
        return level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos).index();
    }

    public static int moonPhase(Level level, BlockPos pos) {
        return moonPhase(level, Vec3.atCenterOf(pos));
    }
}
