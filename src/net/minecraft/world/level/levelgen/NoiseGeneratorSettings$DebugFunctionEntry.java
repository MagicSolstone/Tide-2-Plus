package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public record NoiseGeneratorSettings$DebugFunctionEntry(String label, DensityFunction function) {
   public static final Codec<NoiseGeneratorSettings$DebugFunctionEntry> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.STRING.fieldOf("label").forGetter(NoiseGeneratorSettings$DebugFunctionEntry::label),
            DensityFunction.CODEC.fieldOf("function").forGetter(NoiseGeneratorSettings$DebugFunctionEntry::function)
         )
         .apply(i, NoiseGeneratorSettings$DebugFunctionEntry::new)
   );
}
