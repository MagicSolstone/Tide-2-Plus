package net.minecraft.world.level.levelgen.densityfunction.op;

import com.mojang.serialization.Codec;
import net.minecraft.util.BoundedFloatFunction;
import net.minecraft.util.Interval;
import net.minecraft.util.Unit;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public record SplineFunction$Coordinate(DensityFunction function) implements BoundedFloatFunction<Unit> {
   public static final Codec<SplineFunction$Coordinate> CODEC = DensityFunction.CODEC.xmap(SplineFunction$Coordinate::new, SplineFunction$Coordinate::function);

   public float apply(final Unit point) {
      return 0.0F;
   }

   @Override
   public Interval range() {
      return this.function.range();
   }
}
