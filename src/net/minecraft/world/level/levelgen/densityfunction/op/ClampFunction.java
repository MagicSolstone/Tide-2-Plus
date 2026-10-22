package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;

public record ClampFunction(DensityFunction input, float min, float max) implements DensityFunction {
   public static final MapCodec<ClampFunction> CODEC = RecordCodecBuilder.mapCodec(
         i -> i.group(
               DensityFunction.CODEC.fieldOf("input").forGetter(ClampFunction::input),
               DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min").forGetter(ClampFunction::min),
               DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max").forGetter(ClampFunction::max)
            )
            .apply(i, ClampFunction::new)
      )
      .validate(ClampFunction::validate);

   private static DataResult<ClampFunction> validate(final ClampFunction clamp) {
      return clamp.max < clamp.min
         ? DataResult.error(() -> "min (" + clamp.min + ") must be less than or equal to max (" + clamp.max + ")")
         : DataResult.success(clamp);
   }

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return new ClampFunction$Sampler(this.input.compileSampler(context), this.min, this.max);
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      return input == this.input ? this : new ClampFunction(input, this.min, this.max);
   }

   @Override
   public MapCodec<ClampFunction> codec() {
      return CODEC;
   }

   @Override
   public Interval range() {
      return Interval.clamp(this.input.range(), this.min, this.max);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.input.domainAxes();
   }
}
