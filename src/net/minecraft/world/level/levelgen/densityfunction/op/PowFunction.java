package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.generator.ConstantFunction;

public record PowFunction(DensityFunction base, DensityFunction exponent) implements DensityFunction {
   public static final MapCodec<PowFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("base").forGetter(PowFunction::base), DensityFunction.CODEC.fieldOf("exponent").forGetter(PowFunction::exponent)
         )
         .apply(i, PowFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      DensitySampler base = this.base.compileSampler(context);
      DensitySampler exponent = this.exponent.compileSampler(context);
      if (this.base instanceof ConstantFunction(float var15)) {
         float var9 = var15;
         if (true) {
            return new PowFunction$ConstBaseSampler((double)var9, exponent);
         }
      }

      if (this.exponent instanceof ConstantFunction(float var17)) {
         float var14 = var17;
         if (true) {
            return compileConstExponent(base, var14);
         }
      }

      return new PowFunction$Sampler(base, exponent);
   }

   private static DensitySampler compileConstExponent(final DensitySampler base, final float exponent) {
      float absExponent = Math.abs(exponent);
      DensitySampler specialSampler;
      if (absExponent == 0.5F) {
         specialSampler = new UnaryFunction$SqrtSampler(base);
      } else if (absExponent == 1.0F) {
         specialSampler = base;
      } else if (absExponent == 2.0F) {
         specialSampler = new UnaryFunction$SquareSampler(base);
      } else {
         if (absExponent != 3.0F) {
            return new PowFunction$ConstExponentSampler(base, (double)exponent);
         }

         specialSampler = new UnaryFunction$CubeSampler(base);
      }

      return (DensitySampler)(exponent >= 0.0F ? specialSampler : new UnaryFunction$ReciprocalSampler(specialSampler));
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction base = rule.rewrite(this.base);
      DensityFunction exponent = rule.rewrite(this.exponent);
      return base == this.base && exponent == this.exponent ? this : new PowFunction(base, exponent);
   }

   @Override
   public Interval range() {
      return Interval.pow(this.base.range(), this.exponent.range());
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.base.domainAxes() | this.exponent.domainAxes();
   }

   @Override
   public MapCodec<PowFunction> codec() {
      return CODEC;
   }
}
