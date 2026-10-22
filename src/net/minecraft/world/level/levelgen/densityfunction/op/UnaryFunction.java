package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;

public record UnaryFunction(UnaryFunction$Type type, DensityFunction input) implements DensityFunction {
   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      DensitySampler input = this.input.compileSampler(context);

      return (DensitySampler)(switch (this.type) {
         case ABS -> new UnaryFunction$AbsSampler(input);
         case SQUARE -> new UnaryFunction$SquareSampler(input);
         case CUBE -> new UnaryFunction$CubeSampler(input);
         case SQRT -> new UnaryFunction$SqrtSampler(input);
         case HALF_NEGATIVE -> new UnaryFunction$LeakyReLUSampler(input, 0.5F);
         case QUARTER_NEGATIVE -> new UnaryFunction$LeakyReLUSampler(input, 0.25F);
         case RECIPROCAL -> new UnaryFunction$ReciprocalSampler(input);
         case NEGATE -> new UnaryFunction$NegateSampler(input);
         case SQUEEZE -> new UnaryFunction$SqueezeSampler(input);
         case LOG -> new UnaryFunction$LogSampler(input);
         case SIGN -> new UnaryFunction$SignSampler(input);
      });
   }

   public UnaryFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      return input == this.input ? this : new UnaryFunction(this.type, input);
   }

   @Override
   public MapCodec<UnaryFunction> codec() {
      return this.type.codec;
   }

   @Override
   public Interval range() {
      Interval input = this.input.range();

      return switch (this.type) {
         case ABS -> Interval.abs(input);
         case SQUARE -> Interval.square(input);
         case CUBE -> Interval.mapMonotonic(input, Mth::cube);
         case SQRT -> Interval.pow(input, Interval.ofExact(0.5F));
         case HALF_NEGATIVE -> Interval.mapMonotonic(input, value -> UnaryFunction$LeakyReLUSampler.apply(0.5F, value));
         case QUARTER_NEGATIVE -> Interval.mapMonotonic(input, value -> UnaryFunction$LeakyReLUSampler.apply(0.25F, value));
         case RECIPROCAL -> Interval.reciprocal(input);
         case NEGATE -> Interval.sub(Interval.ofExact(0.0F), input);
         case SQUEEZE -> Interval.mapMonotonic(input, UnaryFunction$SqueezeSampler::apply);
         case LOG -> Interval.log(input);
         case SIGN -> Interval.sign(input);
      };
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.input.domainAxes();
   }
}
