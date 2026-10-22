package net.minecraft.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep$Decoration;

public record Structure$StructureSettings(
   HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep$Decoration step, TerrainAdjustment terrainAdaptation
) {
   private static final Structure$StructureSettings DEFAULT = new Structure$StructureSettings(
      HolderSet.empty(), Map.of(), GenerationStep$Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE
   );
   public static final MapCodec<Structure$StructureSettings> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            RegistryCodecs.holderSet(Registries.BIOME).fieldOf("biomes").forGetter(Structure$StructureSettings::biomes),
            Codec.simpleMap(MobCategory.CODEC, StructureSpawnOverride.CODEC, StringRepresentable.keys(MobCategory.values()))
               .fieldOf("spawn_overrides")
               .forGetter(Structure$StructureSettings::spawnOverrides),
            GenerationStep$Decoration.CODEC.fieldOf("step").forGetter(Structure$StructureSettings::step),
            TerrainAdjustment.CODEC.optionalFieldOf("terrain_adaptation", DEFAULT.terrainAdaptation).forGetter(Structure$StructureSettings::terrainAdaptation)
         )
         .apply(i, Structure$StructureSettings::new)
   );

   public Structure$StructureSettings(final HolderSet<Biome> biomes) {
      this(biomes, DEFAULT.spawnOverrides, DEFAULT.step, DEFAULT.terrainAdaptation);
   }
}
