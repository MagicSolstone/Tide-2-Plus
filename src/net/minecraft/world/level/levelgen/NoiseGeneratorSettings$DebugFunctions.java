package net.minecraft.world.level.levelgen;

import com.mojang.serialization.Codec;
import java.util.List;

public record NoiseGeneratorSettings$DebugFunctions(List<NoiseGeneratorSettings$DebugFunctionEntry> functions) {
   public static final NoiseGeneratorSettings$DebugFunctions EMPTY = new NoiseGeneratorSettings$DebugFunctions(List.of());
   public static final Codec<NoiseGeneratorSettings$DebugFunctions> CODEC = NoiseGeneratorSettings$DebugFunctionEntry.CODEC
      .listOf()
      .xmap(NoiseGeneratorSettings$DebugFunctions::new, NoiseGeneratorSettings$DebugFunctions::functions);
}
