package net.minecraft.world.level.levelgen.densityfunction.generator;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public record EndIslandFunction() implements DensityFunction {
   public static final MapCodec<EndIslandFunction> CODEC = MapCodec.unit(new EndIslandFunction());
   private static final float ISLAND_THRESHOLD = -0.9F;

   private static float getHeightValue(final SimplexNoise islandNoise, final int sectionX, final int sectionZ) {
      int chunkX = sectionX / 2;
      int chunkZ = sectionZ / 2;
      int subSectionX = sectionX % 2;
      int subSectionZ = sectionZ % 2;
      float doffs = -100.0F;

      for (int xo = -12; xo <= 12; xo++) {
         for (int zo = -12; zo <= 12; zo++) {
            long totalChunkX = chunkX + xo;
            long totalChunkZ = chunkZ + zo;
            if (totalChunkX * totalChunkX + totalChunkZ * totalChunkZ > 4096L && islandNoise.get(totalChunkX, totalChunkZ) < -0.9F) {
               float islandSize = (Mth.abs((float)totalChunkX) * 3439.0F + Mth.abs((float)totalChunkZ) * 147.0F) % 13.0F + 9.0F;
               float xd = subSectionX - xo * 2;
               float zd = subSectionZ - zo * 2;
               float newDoffs = 100.0F - Mth.sqrt(xd * xd + zd * zd) * islandSize;
               newDoffs = Mth.clamp(newDoffs, -100.0F, 80.0F);
               doffs = Math.max(doffs, newDoffs);
            }
         }
      }

      return doffs;
   }

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      RandomSource islandRandom = context.createEndIslandRandom();
      islandRandom.consumeCount(17292);
      SimplexNoise islandNoise = new SimplexNoise(islandRandom, true);
      return new EndIslandFunction$Sampler(islandNoise);
   }

   @Override
   public Interval range() {
      return Interval.of(-0.84375F, 0.5625F);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return 5;
   }

   @Override
   public MapCodec<EndIslandFunction> codec() {
      return CODEC;
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      return this;
   }
}
