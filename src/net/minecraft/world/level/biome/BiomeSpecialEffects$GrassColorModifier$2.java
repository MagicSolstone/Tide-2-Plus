package net.minecraft.world.level.biome;

import net.minecraft.util.ARGB;

enum BiomeSpecialEffects$GrassColorModifier$2 {
   private BiomeSpecialEffects$GrassColorModifier$2(final String name) {
   }

   @Override
   public int modifyColor(final double x, final double z, final int baseColor) {
      return ARGB.opaque((baseColor & 16711422) + 2634762 >> 1);
   }
}
