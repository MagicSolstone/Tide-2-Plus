package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

public class MultiNoiseBiomeSourceParameterList {
   public static final Codec<MultiNoiseBiomeSourceParameterList> DIRECT_CODEC = RecordCodecBuilder.create(
      i -> i.group(MultiNoiseBiomeSourceParameterList$Preset.CODEC.fieldOf("preset").forGetter(e -> e.preset), RegistryOps.retrieveGetter(Registries.BIOME))
         .apply(i, MultiNoiseBiomeSourceParameterList::new)
   );
   public static final Codec<Holder<MultiNoiseBiomeSourceParameterList>> CODEC = RegistryCodecs.holder(
      Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, DIRECT_CODEC
   );
   private final MultiNoiseBiomeSourceParameterList$Preset preset;
   private final Climate$ParameterList<Holder<Biome>> parameters;

   public MultiNoiseBiomeSourceParameterList(final MultiNoiseBiomeSourceParameterList$Preset preset, final HolderGetter<Biome> biomes) {
      this.preset = preset;
      this.parameters = preset.provider.apply(biomes::getOrThrow);
   }

   public Climate$ParameterList<Holder<Biome>> parameters() {
      return this.parameters;
   }

   public static Map<MultiNoiseBiomeSourceParameterList$Preset, Climate$ParameterList<ResourceKey<Biome>>> knownPresets() {
      return MultiNoiseBiomeSourceParameterList$Preset.BY_NAME
         .values()
         .stream()
         .collect(Collectors.toMap(e -> (MultiNoiseBiomeSourceParameterList$Preset)e, e -> e.provider().apply(k -> k)));
   }
}
