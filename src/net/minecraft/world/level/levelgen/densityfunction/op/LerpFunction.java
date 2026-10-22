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

public record LerpFunction(DensityFunction alpha, DensityFunction first, DensityFunction second) implements DensityFunction {
   public static final MapCodec<LerpFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("alpha").forGetter(LerpFunction::alpha),
            DensityFunction.CODEC.fieldOf("first").forGetter(LerpFunction::first),
            DensityFunction.CODEC.fieldOf("second").forGetter(LerpFunction::second)
         )
         .apply(i, LerpFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      DensitySampler alpha = this.alpha.compileSampler(context);
      DensitySampler first = this.first.compileSampler(context);
      DensitySampler second = this.second.compileSampler(context);
      if (this.first instanceof ConstantFunction(float var16)) {
         float var10 = var16;
         if (true) {
            return new LerpFunction$ConstFirstSampler(alpha, var10, second);
         }
      }

      if (this.second instanceof ConstantFunction(float var18)) {
         float var15 = var18;
         if (true) {
            return new LerpFunction$ConstSecondSampler(alpha, first, var15);
         }
      }

      return new LerpFunction$Sampler(alpha, first, second);
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction alpha = rule.rewrite(this.alpha);
      DensityFunction first = rule.rewrite(this.first);
      DensityFunction second = rule.rewrite(this.second);
      return alpha == this.alpha && first == this.first && second == this.second ? this : new LerpFunction(alpha, first, second);
   }

   @Override
   public Interval range() {
      return Interval.lerp(this.alpha.range(), this.first.range(), this.second.range());
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.alpha.domainAxes() | this.first.domainAxes() | this.second.domainAxes();
   }

   @Override
   public MapCodec<LerpFunction> codec() {
      return CODEC;
   }
}
