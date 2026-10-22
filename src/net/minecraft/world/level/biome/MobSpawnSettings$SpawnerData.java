package net.minecraft.world.level.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;

public record MobSpawnSettings$SpawnerData(EntityType<?> type, IntProvider count) {
   public static final MapCodec<MobSpawnSettings$SpawnerData> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(MobSpawnSettings$SpawnerData::type),
            IntProviders.CODEC.fieldOf("count").forGetter(MobSpawnSettings$SpawnerData::count)
         )
         .apply(i, MobSpawnSettings$SpawnerData::new)
   );

   public MobSpawnSettings$SpawnerData(EntityType<?> type, IntProvider count) {
      type = type.getCategory() == MobCategory.MISC ? EntityTypes.PIG : type;
      this.type = type;
      this.count = count;
   }

   @Override
   public String toString() {
      return EntityType.getKey(this.type)
         + "*("
         + this.count.minInclusive()
         + "-"
         + this.count.maxInclusive()
         + ") ("
         + BuiltInRegistries.INT_PROVIDER_TYPE.getKey(this.count.codec())
         + ")";
   }
}
