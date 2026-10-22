package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.util.BoundedFloatFunction;
import net.minecraft.util.Interval;
import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;

record SplineFunction$SamplerCoordinate(DensitySampler sampler, int index, Interval range) implements BoundedFloatFunction<SplineFunction$SplineInput> {
   public float apply(final SplineFunction$SplineInput input) {
      return input.sampleCoordinate(this.sampler, this.index);
   }
}
