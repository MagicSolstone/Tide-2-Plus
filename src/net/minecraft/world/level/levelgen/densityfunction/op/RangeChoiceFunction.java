package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunctions;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.generator.ConstantFunction;

public record RangeChoiceFunction(DensityFunction input, float minInclusive, float maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange)
   implements DensityFunction {
   public static final MapCodec<RangeChoiceFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("input").forGetter(RangeChoiceFunction::input),
            DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min_inclusive").forGetter(RangeChoiceFunction::minInclusive),
            DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max_exclusive").forGetter(RangeChoiceFunction::maxExclusive),
            DensityFunction.CODEC.fieldOf("when_in_range").forGetter(RangeChoiceFunction::whenInRange),
            DensityFunction.CODEC.fieldOf("when_out_of_range").forGetter(RangeChoiceFunction::whenOutOfRange)
         )
         .apply(i, RangeChoiceFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      DensitySampler input = this.input.compileSampler(context);
      if (this.whenInRange instanceof ConstantFunction(float var14)) {
         float var8 = var14;
         if (true && this.whenOutOfRange instanceof ConstantFunction(float var16)) {
            var8 = var16;
            if (true) {
               return new RangeChoiceFunction$ConstSampler(input, this.minInclusive, this.maxExclusive, var8, var8);
            }
         }
      }

      return new RangeChoiceFunction$Sampler(
         input, this.minInclusive, this.maxExclusive, this.whenInRange.compileSampler(context), this.whenOutOfRange.compileSampler(context)
      );
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      DensityFunction whenInRange = rule.rewrite(this.whenInRange);
      DensityFunction whenOutOfRange = rule.rewrite(this.whenOutOfRange);
      return input == this.input && whenInRange == this.whenInRange && whenOutOfRange == this.whenOutOfRange
         ? this
         : new RangeChoiceFunction(input, this.minInclusive, this.maxExclusive, whenInRange, whenOutOfRange);
   }

   @Override
   public Interval range() {
      return Interval.encapsulating(this.whenInRange.range(), this.whenOutOfRange.range());
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.input.domainAxes() | this.whenInRange.domainAxes() | this.whenOutOfRange.domainAxes();
   }

   @Override
   public MapCodec<RangeChoiceFunction> codec() {
      return CODEC;
   }
}
