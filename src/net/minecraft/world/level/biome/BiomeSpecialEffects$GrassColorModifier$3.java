package net.minecraft.world.level.biome;

enum BiomeSpecialEffects$GrassColorModifier$3 {
   private BiomeSpecialEffects$GrassColorModifier$3(final String name) {
   }

   @Override
   public int modifyColor(final double x, final double z, final int baseColor) {
      double groundValue = Biome.BIOME_INFO_NOISE.get(x * 0.0225, z * 0.0225);
      return groundValue < -0.1 ? -11766212 : -9801671;
   }
}
