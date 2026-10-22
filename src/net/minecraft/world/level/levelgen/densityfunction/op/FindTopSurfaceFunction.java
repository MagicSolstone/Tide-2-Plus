package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Interval;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;

public record FindTopSurfaceFunction(DensityFunction density, DensityFunction upperBound, int lowerBound, int cellHeight) implements DensityFunction {
   public static final MapCodec<FindTopSurfaceFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("density").forGetter(FindTopSurfaceFunction::density),
            DensityFunction.CODEC.fieldOf("upper_bound").forGetter(FindTopSurfaceFunction::upperBound),
            Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("lower_bound").forGetter(FindTopSurfaceFunction::lowerBound),
            ExtraCodecs.POSITIVE_INT.fieldOf("cell_height").forGetter(FindTopSurfaceFunction::cellHeight)
         )
         .apply(i, FindTopSurfaceFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      FindTopSurfaceFunction$Sampler sampler = new FindTopSurfaceFunction$Sampler(
         this.density.compileSampler(context), this.upperBound.compileSampler(context), this.lowerBound, this.cellHeight
      );
      return new SliceFunction$YSampler(sampler, 0);
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      DensityFunction density = rule.rewrite(this.density);
      DensityFunction upperBound = rule.rewrite(this.upperBound);
      return density == this.density && upperBound == this.upperBound
         ? this
         : new FindTopSurfaceFunction(density, upperBound, this.lowerBound, this.cellHeight);
   }

   @Override
   public Interval range() {
      return Interval.of(this.lowerBound, Math.max((float)this.lowerBound, this.upperBound.range().max()));
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return (this.density.domainAxes() | this.upperBound.domainAxes()) & -3;
   }

   @Override
   public MapCodec<FindTopSurfaceFunction> codec() {
      return CODEC;
   }
}
