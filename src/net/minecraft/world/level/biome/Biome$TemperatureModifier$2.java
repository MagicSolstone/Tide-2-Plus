package net.minecraft.world.level.biome;

import net.minecraft.core.BlockPos;

enum Biome$TemperatureModifier$2 {
   private Biome$TemperatureModifier$2(final String name) {
   }

   @Override
   public float modifyTemperature(final BlockPos pos, final float baseTemperature) {
      double groundValueLargeVariation = Biome.FROZEN_TEMPERATURE_NOISE.get(pos.getX() * 0.05, pos.getZ() * 0.05) * 7.0F;
      double groundValueEdgeVariation = Biome.BIOME_INFO_NOISE.get(pos.getX() * 0.2, pos.getZ() * 0.2);
      double icePatches = groundValueLargeVariation + groundValueEdgeVariation;
      if (icePatches < 0.3) {
         double groundValueSmallVariation = Biome.BIOME_INFO_NOISE.get(pos.getX() * 0.09, pos.getZ() * 0.09);
         if (groundValueSmallVariation < 0.8) {
            return 0.2F;
         }
      }

      return baseTemperature;
   }
}
