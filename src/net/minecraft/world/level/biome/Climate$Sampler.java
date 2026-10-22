package net.minecraft.world.level.biome;

import net.minecraft.core.QuartPos;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler$Bound;

public record Climate$Sampler(
   DensitySampler$Bound temperature,
   DensitySampler$Bound humidity,
   DensitySampler$Bound continentalness,
   DensitySampler$Bound erosion,
   DensitySampler$Bound depth,
   DensitySampler$Bound weirdness
) {
   public Climate$TargetPoint sample(final int quartX, final int quartY, final int quartZ) {
      int blockX = QuartPos.toBlock(quartX);
      int blockY = QuartPos.toBlock(quartY);
      int blockZ = QuartPos.toBlock(quartZ);
      return Climate.target(
         this.temperature.sampleValue(blockX, blockY, blockZ),
         this.humidity.sampleValue(blockX, blockY, blockZ),
         this.continentalness.sampleValue(blockX, blockY, blockZ),
         this.erosion.sampleValue(blockX, blockY, blockZ),
         this.depth.sampleValue(blockX, blockY, blockZ),
         this.weirdness.sampleValue(blockX, blockY, blockZ)
      );
   }
}
