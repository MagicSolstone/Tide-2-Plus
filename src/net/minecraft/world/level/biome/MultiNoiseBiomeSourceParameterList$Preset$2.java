package net.minecraft.world.level.biome;

import java.util.function.Function;
import net.minecraft.resources.ResourceKey;

class MultiNoiseBiomeSourceParameterList$Preset$2 implements MultiNoiseBiomeSourceParameterList$Preset$SourceProvider {
   @Override
   public <T> Climate$ParameterList<T> apply(final Function<ResourceKey<Biome>, T> lookup) {
      return MultiNoiseBiomeSourceParameterList$Preset.generateOverworldBiomes(lookup);
   }
}
