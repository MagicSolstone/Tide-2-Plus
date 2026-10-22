package net.minecraft.world.item.enchantment.effects;

import net.minecraft.util.RandomSource;

@FunctionalInterface
interface SpawnParticlesEffect$PositionSourceType$CoordinateSource {
   double getCoordinate(double pos, double center, float boundingBoxSpan, RandomSource random);
}
