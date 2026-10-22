package net.minecraft.world.level.biome;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public record MultiNoiseBiomeSourceParameterList$Preset(Identifier id, MultiNoiseBiomeSourceParameterList$Preset$SourceProvider provider) {
   public static final MultiNoiseBiomeSourceParameterList$Preset NETHER = new MultiNoiseBiomeSourceParameterList$Preset(
      Identifier.withDefaultNamespace("nether"), new MultiNoiseBiomeSourceParameterList$Preset$1()
   );
   public static final MultiNoiseBiomeSourceParameterList$Preset OVERWORLD = new MultiNoiseBiomeSourceParameterList$Preset(
      Identifier.withDefaultNamespace("overworld"), new MultiNoiseBiomeSourceParameterList$Preset$2()
   );
   private static final Map<Identifier, MultiNoiseBiomeSourceParameterList$Preset> BY_NAME = Stream.of(NETHER, OVERWORLD)
      .collect(Collectors.toMap(MultiNoiseBiomeSourceParameterList$Preset::id, p -> (MultiNoiseBiomeSourceParameterList$Preset)p));
   public static final Codec<MultiNoiseBiomeSourceParameterList$Preset> CODEC = Identifier.CODEC
      .flatXmap(
         name -> Optional.ofNullable(BY_NAME.get(name)).<DataResult>map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown preset: " + name)),
         p -> DataResult.success(p.id)
      );

   private static <T> Climate$ParameterList<T> generateOverworldBiomes(final Function<ResourceKey<Biome>, T> lookup) {
      Builder<Pair<Climate$ParameterPoint, T>> builder = ImmutableList.builder();
      new OverworldBiomeBuilder().addBiomes(p -> builder.add(p.mapSecond(lookup)));
      return new Climate$ParameterList<>(builder.build());
   }

   public Stream<ResourceKey<Biome>> usedBiomes() {
      return this.provider.apply(e -> e).values().stream().<ResourceKey<Biome>>map(Pair::getSecond).distinct();
   }
}
