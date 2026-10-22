package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;

public record InterpolatedFunction(DensityFunction input, int cellSizeXz, int cellSizeY) implements DensityFunction {
   public static final MapCodec<InterpolatedFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("input").forGetter(InterpolatedFunction::input),
            ExtraCodecs.POSITIVE_INT.fieldOf("cell_size_xz").forGetter(InterpolatedFunction::cellSizeXz),
            ExtraCodecs.POSITIVE_INT.fieldOf("cell_size_y").forGetter(InterpolatedFunction::cellSizeY)
         )
         .apply(i, InterpolatedFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return new InterpolatedFunction$Sampler(
         this.input.compileSampler(context), this.cellSizeXz, this.cellSizeY, 1.0F / this.cellSizeXz, 1.0F / this.cellSizeY
      );
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction input = rule.rewrite(this.input);
      return input == this.input ? this : new InterpolatedFunction(input, this.cellSizeXz, this.cellSizeY);
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
   public MapCodec<InterpolatedFunction> codec() {
      return CODEC;
   }
}
