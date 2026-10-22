package com.li64.tide.compat.seasons;

import net.minecraft.world.level.Level;


public class SeasonsCompat {
    // This fork has no seasons-mod integrations: it's always spring, and seasons are never active.
    public static Season getSeason(Level level) {
        return Season.SPRING;
    }

    public static boolean isActive() {
        return false;
    }
}