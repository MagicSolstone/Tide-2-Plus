package net.minecraft.world.level.biome;

import net.minecraft.core.BlockPos;

enum Biome$TemperatureModifier$1 {
   private Biome$TemperatureModifier$1(final String name) {
   }

   @Override
   public float modifyTemperature(final BlockPos pos, final float baseTemperature) {
      return baseTemperature;
   }
}
