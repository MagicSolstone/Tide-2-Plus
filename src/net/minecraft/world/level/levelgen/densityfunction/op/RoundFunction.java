package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.generator.ConstantFunction;

public record RoundFunction(RoundFunction$Type type, DensityFunction input, DensityFunction multiple) implements DensityFunction {
   private static float roundToInteger(final float input, final RoundFunction$Type type) {
      return switch (type) {
         case FLOOR -> (float)Math.floor(input);
         case ROUND -> Math.round(input);
         case CEIL -> (float)Math.ceil(input);
         case TRUNCATE -> input > 0.0F ? (float)Math.floor(input) : (float)Math.ceil(input);
      };
   }

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      DensitySampler input = this.input.compileSampler(context);
      if (this.multiple instanceof ConstantFunction(float var9)) {
         float var6 = var9;
         if (true && var6 == 1.0F) {
            return new RoundFunction$IntegerMultipleSampler(this.type, input);
         }
      }

      return new RoundFunction$Sampler(this.type, input, this.multiple.compileSampler(context));
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      DensityFunction multiple = rule.rewrite(this.multiple);
      return input == this.input && multiple == this.multiple ? this : new RoundFunction(this.type, input, multiple);
   }

   @Override
   public Interval range() {
      Interval multipleRange = this.multiple.range();
      return Interval.mul(Interval.mapMonotonic(Interval.div(this.input.range(), multipleRange), value -> roundToInteger(value, this.type)), multipleRange);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.input.domainAxes() | this.multiple.domainAxes();
   }

   @Override
   public MapCodec<RoundFunction> codec() {
      return this.type.codec;
   }
}
