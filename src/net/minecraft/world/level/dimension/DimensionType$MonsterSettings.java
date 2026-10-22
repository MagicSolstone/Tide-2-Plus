package net.minecraft.world.level.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;

public record DimensionType$MonsterSettings(IntProvider monsterSpawnLightTest, int monsterSpawnBlockLightLimit) {
   public static final MapCodec<DimensionType$MonsterSettings> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            IntProviders.codec(0, 15).fieldOf("monster_spawn_light_level").forGetter(DimensionType$MonsterSettings::monsterSpawnLightTest),
            Codec.intRange(0, 15).fieldOf("monster_spawn_block_light_limit").forGetter(DimensionType$MonsterSettings::monsterSpawnBlockLightLimit)
         )
         .apply(i, DimensionType$MonsterSettings::new)
   );
}
