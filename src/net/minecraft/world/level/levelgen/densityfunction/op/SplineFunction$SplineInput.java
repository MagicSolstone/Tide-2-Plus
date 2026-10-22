package net.minecraft.world.level.levelgen.densityfunction.op;

import net.minecraft.world.level.levelgen.densityfunction.DensitySampler;

@FunctionalInterface
interface SplineFunction$SplineInput {
   float sampleCoordinate(DensitySampler coordinateSampler, int coordinateIndex);
}
