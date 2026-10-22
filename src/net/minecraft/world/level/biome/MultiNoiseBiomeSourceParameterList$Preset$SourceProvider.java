package net.minecraft.world.level.biome;

import java.util.function.Function;
import net.minecraft.resources.ResourceKey;

@FunctionalInterface
interface MultiNoiseBiomeSourceParameterList$Preset$SourceProvider {
   <T> Climate$ParameterList<T> apply(final Function<ResourceKey<Biome>, T> lookup);
}
