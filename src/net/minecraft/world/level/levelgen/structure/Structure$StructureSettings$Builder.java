package net.minecraft.world.level.levelgen.structure;

import java.util.Map;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep$Decoration;

public class Structure$StructureSettings$Builder {
   private final HolderSet<Biome> biomes;
   private Map<MobCategory, StructureSpawnOverride> spawnOverrides;
   private GenerationStep$Decoration step;
   private TerrainAdjustment terrainAdaption;

   public Structure$StructureSettings$Builder(final HolderSet<Biome> biomes) {
      this.spawnOverrides = Structure$StructureSettings.DEFAULT.spawnOverrides;
      this.step = Structure$StructureSettings.DEFAULT.step;
      this.terrainAdaption = Structure$StructureSettings.DEFAULT.terrainAdaptation;
      this.biomes = biomes;
   }

   public Structure$StructureSettings$Builder spawnOverrides(final Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
      this.spawnOverrides = spawnOverrides;
      return this;
   }

   public Structure$StructureSettings$Builder generationStep(final GenerationStep$Decoration step) {
      this.step = step;
      return this;
   }

   public Structure$StructureSettings$Builder terrainAdapation(final TerrainAdjustment terrainAdaption) {
      this.terrainAdaption = terrainAdaption;
      return this;
   }

   public Structure$StructureSettings build() {
      return new Structure$StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption);
   }
}
