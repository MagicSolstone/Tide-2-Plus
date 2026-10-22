package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;
import net.minecraft.world.level.levelgen.densityfunction.DensitySamplerSet;
import net.minecraft.world.level.levelgen.densityfunction.DensityVolume;

public record Aquifer$Config(
   DensityFunction barrierNoise,
   DensityFunction fluidLevelFloodednessNoise,
   DensityFunction fluidLevelSpreadNoise,
   DensityFunction lavaNoise,
   DensityFunction exclusion,
   DensityFunction surfaceLevel
) {
   public static final Codec<Aquifer$Config> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            DensityFunction.CODEC.fieldOf("barrier").forGetter(Aquifer$Config::barrierNoise),
            DensityFunction.CODEC.fieldOf("fluid_level_floodedness").forGetter(Aquifer$Config::fluidLevelFloodednessNoise),
            DensityFunction.CODEC.fieldOf("fluid_level_spread").forGetter(Aquifer$Config::fluidLevelSpreadNoise),
            DensityFunction.CODEC.fieldOf("lava").forGetter(Aquifer$Config::lavaNoise),
            DensityFunction.CODEC.fieldOf("exclusion").forGetter(Aquifer$Config::exclusion),
            DensityFunction.CODEC.fieldOf("surface_level").forGetter(Aquifer$Config::surfaceLevel)
         )
         .apply(i, Aquifer$Config::new)
   );

   public Aquifer create(
      final DensitySamplerSet cachingSamplers,
      final PositionalRandomFactory positionalRandomFactory,
      final DensityVolume volume,
      final Aquifer$FluidPicker fluidRule
   ) {
      return new Aquifer$NoiseBasedAquifer(cachingSamplers, this, positionalRandomFactory, volume, fluidRule);
   }
}
