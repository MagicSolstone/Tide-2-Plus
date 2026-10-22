package net.minecraft.world.level.levelgen.densityfunction;

import org.jspecify.annotations.Nullable;

class SamplerContext$CacheCell {
   @Nullable
   private DensityVolume volume;
   @Nullable
   private ScopedDensityBuffer buffer;
   private long valueKey;
   private float value = Float.NaN;

   private SamplerContext$CacheCell() {
   }
}
