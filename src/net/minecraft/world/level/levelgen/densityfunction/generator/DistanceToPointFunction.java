package net.minecraft.world.level.levelgen.densityfunction.generator;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$Axes;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction$CompileContext;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;
import net.minecraft.world.level.levelgen.densityfunction.DfRewriteRule;
import net.minecraft.world.level.levelgen.densityfunction.DistanceMetric;

public record DistanceToPointFunction(Vec3i point, DistanceMetric metric) implements DensityFunction {
   public static final MapCodec<DistanceToPointFunction> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Vec3i.CODEC.fieldOf("point").forGetter(DistanceToPointFunction::point),
            DistanceMetric.CODEC.fieldOf("metric").forGetter(DistanceToPointFunction::metric)
         )
         .apply(i, DistanceToPointFunction::new)
   );

   @Override
   public DensitySampler compileSampler(final DensityFunction$CompileContext context) {
      return new DistanceToPointFunction$Sampler(this.point.getX(), this.point.getY(), this.point.getZ(), this.metric);
   }

   @Override
   public Interval range() {
      return Interval.of(0.0F, Float.POSITIVE_INFINITY);
   }

   @DensityFunction$Axes
   @Override
   public int domainAxes() {
      return 7;
   }

   @Override
   public MapCodec<DistanceToPointFunction> codec() {
      return CODEC;
   }

   @Override
   public DensityFunction rewriteChildren(final DfRewriteRule rule) {
      return this;
   }
}
