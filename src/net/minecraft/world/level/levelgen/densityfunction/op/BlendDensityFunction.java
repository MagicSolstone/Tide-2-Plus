package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;

public record BlendDensityFunction(DensityFunction input) implements DensityFunction {
   public static final MapCodec<BlendDensityFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(DensityFunction.CODEC.fieldOf("input").forGetter(BlendDensityFunction::input)).apply(i, BlendDensityFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return new BlendDensityFunction$Sampler(this.input.compileSampler(context));
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      return input == this.input ? this : new BlendDensityFunction(input);
   }

   @Override
   public Interval range() {
      return this.input.range();
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return this.input.domainAxes();
   }

   @Override
   public MapCodec<BlendDensityFunction> codec() {
      return CODEC;
   }
}
