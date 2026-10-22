package net.minecraft.world.level.levelgen.synth;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Interval;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.generator.NoiseFunction$Sampler;
import net.minecraft.world.level.levelgen.densityfunction.op.BinaryFunction$ConstAddSampler;
import net.minecraft.world.level.levelgen.densityfunction.op.ClampFunction$Sampler;
import net.minecraft.world.level.levelgen.densityfunction.op.LerpFunction$Sampler;

public record BlendedNoise(double xzScale, double yScale, double xzFactor, double yFactor, double smearScaleMultiplier) implements DensityFunction {
   private static final Codec<Double> SCALE_RANGE = Codec.doubleRange(0.001, 1000.0);
   public static final MapCodec<BlendedNoise> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            SCALE_RANGE.fieldOf("xz_scale").forGetter(BlendedNoise::xzScale),
            SCALE_RANGE.fieldOf("y_scale").forGetter(BlendedNoise::yScale),
            SCALE_RANGE.fieldOf("xz_factor").forGetter(BlendedNoise::xzFactor),
            SCALE_RANGE.fieldOf("y_factor").forGetter(BlendedNoise::yFactor),
            Codec.doubleRange(1.0, 8.0).fieldOf("smear_scale_multiplier").forGetter(BlendedNoise::smearScaleMultiplier)
         )
         .apply(i, BlendedNoise::new)
   );
   private static final double BASE_SCALE = 684.412;
   private static final double LIMIT_FACTOR = 0.99998474F;
   private static final double MAIN_FACTOR = 12.75;
   private static final int LIMIT_FIRST_OCTAVE = -15;
   private static final int MAIN_FIRST_OCTAVE = -7;
   public static final Identifier NOISE_SEED = Identifier.withDefaultNamespace("terrain");

   private double xzMultiplier() {
      return 684.412 * this.xzScale;
   }

   private double yMultiplier() {
      return 684.412 * this.yScale;
   }

   public BlendedNoise$FbmSet createFbmSet(final RandomSource random) {
      double limitSmearScaleY = this.yMultiplier() * this.smearScaleMultiplier;
      double mainSmearScaleY = limitSmearScaleY / this.yFactor;
      return new BlendedNoise$FbmSet(
         createFbm(random, -15, limitSmearScaleY, 0.99998474F),
         createFbm(random, -15, limitSmearScaleY, 0.99998474F),
         createFbm(random, -7, mainSmearScaleY, 12.75)
      );
   }

   @VisibleForTesting
   public static NoiseStack createFbm(final RandomSource random, final int firstOctave, final double smearScaleY, double valueFactor) {
      if (firstOctave > 0) {
         throw new IllegalArgumentException("firstOctave>0");
      } else {
         int octaves = -firstOctave + 1;
         double factor = 1.0;
         valueFactor /= Math.pow(2.0, octaves) - 1.0;
         NoiseStack$Builder stack = NoiseStack.builder();

         for (int i = octaves - 1; i >= 0; i--) {
            stack.add(new SmearedPerlinNoise(random, smearScaleY * factor), factor, (float)valueFactor);
            factor /= 2.0;
            valueFactor *= 2.0;
         }

         return stack.build();
      }
   }

   private static Interval computeFbmRange(final int firstOctave, final double smearScaleY, double valueFactor) {
      int octaves = -firstOctave + 1;
      double factor = 1.0;
      valueFactor /= Math.pow(2.0, octaves) - 1.0;
      Interval range = Interval.ofExact(0.0F);

      for (int i = octaves - 1; i >= 0; i--) {
         Interval layerRange = Interval.mul(SmearedPerlinNoise.range(smearScaleY * factor), Interval.ofExact((float)valueFactor));
         range = Interval.add(range, layerRange);
         factor /= 2.0;
         valueFactor *= 2.0;
      }

      return range;
   }

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return this.compileSampler(context.createRandom(NOISE_SEED));
   }

   @VisibleForTesting
   public DensitySampler compileSampler(final RandomSource random) {
      BlendedNoise$FbmSet fbms = this.createFbmSet(random);
      double xzMultiplier = this.xzMultiplier();
      double yMultiplier = this.yMultiplier();
      DensitySampler minLimitNoise = new NoiseFunction$Sampler(fbms.minLimitNoise(), xzMultiplier, yMultiplier);
      DensitySampler maxLimitNoise = new NoiseFunction$Sampler(fbms.maxLimitNoise(), xzMultiplier, yMultiplier);
      DensitySampler mainNoise = new NoiseFunction$Sampler(fbms.mainNoise(), xzMultiplier / this.xzFactor, yMultiplier / this.yFactor);
      DensitySampler choice = new ClampFunction$Sampler(new BinaryFunction$ConstAddSampler(mainNoise, 0.5F), 0.0F, 1.0F);
      return new LerpFunction$Sampler(choice, minLimitNoise, maxLimitNoise);
   }

   @Override
   public Interval range() {
      return computeFbmRange(-15, this.yMultiplier() * this.smearScaleMultiplier, 0.99998474F);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return 7;
   }

   @Override
   public MapCodec<BlendedNoise> codec() {
      return CODEC;
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      return this;
   }
}
