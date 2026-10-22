package net.minecraft.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.MobSpawnSettings$SpawnerData;

public record StructureSpawnOverride(StructureSpawnOverride$BoundingBoxType boundingBox, WeightedList<MobSpawnSettings$SpawnerData> spawns) {
   public static final Codec<StructureSpawnOverride> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            StructureSpawnOverride$BoundingBoxType.CODEC.fieldOf("bounding_box").forGetter(StructureSpawnOverride::boundingBox),
            WeightedList.codec(MobSpawnSettings$SpawnerData.CODEC).fieldOf("spawns").forGetter(StructureSpawnOverride::spawns)
         )
         .apply(i, StructureSpawnOverride::new)
   );
}
