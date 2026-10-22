package net.minecraft.world.level.levelgen.densityfunction.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public record NoiseFunction(
   Holder<NormalNoise> noise, @Deprecated double xzScale, double yScale, DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ
) implements DensityFunction {
   public static final MapCodec<NoiseFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            NormalNoise.CODEC.fieldOf("noise").forGetter(NoiseFunction::noise),
            Codec.DOUBLE.fieldOf("xz_scale").forGetter(NoiseFunction::xzScale),
            Codec.DOUBLE.fieldOf("y_scale").forGetter(NoiseFunction::yScale),
            DensityFunction.CODEC.optionalFieldOf("shift_x", DensityFunctions.zero()).forGetter(NoiseFunction::shiftX),
            DensityFunction.CODEC.optionalFieldOf("shift_y", DensityFunctions.zero()).forGetter(NoiseFunction::shiftY),
            DensityFunction.CODEC.optionalFieldOf("shift_z", DensityFunctions.zero()).forGetter(NoiseFunction::shiftZ)
         )
         .apply(i, NoiseFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      Noise noise = context.createNoiseSampler(this.noise);
      if (this.shiftX.equals(DensityFunctions.zero()) && this.shiftY.equals(DensityFunctions.zero()) && this.shiftZ.equals(DensityFunctions.zero())) {
         return new NoiseFunction$Sampler(noise, this.xzScale, this.yScale);
      } else {
         DensitySampler shiftX = this.shiftX.compileSampler(context);
         DensitySampler shiftZ = this.shiftZ.compileSampler(context);
         if (this.shiftY.equals(DensityFunctions.zero())) {
            return new NoiseFunction$ShiftedXzSampler(shiftX, shiftZ, noise, this.xzScale, this.yScale);
         } else {
            DensitySampler shiftY = this.shiftY.compileSampler(context);
            return new NoiseFunction$ShiftedXyzSampler(shiftX, shiftY, shiftZ, noise, this.xzScale, this.yScale);
         }
      }
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction shiftX = rule.rewrite(this.shiftX);
      DensityFunction shiftY = rule.rewrite(this.shiftY);
      DensityFunction shiftZ = rule.rewrite(this.shiftZ);
      return shiftX == this.shiftX && shiftY == this.shiftY && shiftZ == this.shiftZ
         ? this
         : new NoiseFunction(this.noise, this.xzScale, this.yScale, shiftX, shiftY, shiftZ);
   }

   @Override
   public Interval range() {
      return this.noise.value().range();
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      int axes = 7;
      if (this.yScale == 0.0) {
         axes &= -3;
      }

      if (this.xzScale == 0.0) {
         axes &= -6;
      }

      return axes | this.shiftX.domainAxes() | this.shiftY.domainAxes() | this.shiftZ.domainAxes();
   }

   @Override
   public MapCodec<NoiseFunction> codec() {
      return CODEC;
   }
}
