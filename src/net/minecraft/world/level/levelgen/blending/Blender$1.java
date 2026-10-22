package net.minecraft.world.level.levelgen.blending;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.chunk.CarvingMask$Filter;

class Blender$1 extends Blender {
   Blender$1(final Long2ObjectOpenHashMap heightAndBiomeBlendingData, final Long2ObjectOpenHashMap densityBlendingData, final CarvingMask$Filter carvingFilter) {
      super(heightAndBiomeBlendingData, densityBlendingData, carvingFilter);
   }

   @Override
   protected Blender$BlendingOutput blendOffsetAndFactor(final int blockX, final int blockZ) {
      return new Blender$BlendingOutput(1.0F, 0.0F);
   }

   @Override
   public float blendDensity(final int blockX, final int blockY, final int blockZ, final float noiseValue) {
      return noiseValue;
   }

   @Override
   public BiomeResolver getBiomeResolver(final BiomeResolver biomeResolver) {
      return biomeResolver;
   }
}
