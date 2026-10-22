package net.minecraft.world.level.levelgen.densityfunction;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.Noise;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public interface DensityFunction$CompileContext {
   Noise createNoiseSampler(Holder<NormalNoise> parameters);

   RandomSource createRandom(Identifier seed);

   @Deprecated
   RandomSource createEndIslandRandom();
}
