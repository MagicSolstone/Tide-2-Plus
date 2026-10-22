package net.minecraft.world.level.block.entity.trialspawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.storage.loot.LootTable;

public record TrialSpawnerStateData$Packed(
   Set<UUID> detectedPlayers,
   Set<UUID> currentMobs,
   long cooldownEndsAt,
   long nextMobSpawnsAt,
   int totalMobsSpawned,
   Optional<SpawnData> nextSpawnData,
   Optional<ResourceKey<LootTable>> ejectingLootTable
) {
   public static final MapCodec<TrialSpawnerStateData$Packed> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            UUIDUtil.CODEC_SET.lenientOptionalFieldOf("registered_players", Set.of()).forGetter(TrialSpawnerStateData$Packed::detectedPlayers),
            UUIDUtil.CODEC_SET.lenientOptionalFieldOf("current_mobs", Set.of()).forGetter(TrialSpawnerStateData$Packed::currentMobs),
            Codec.LONG.lenientOptionalFieldOf("cooldown_ends_at", 0L).forGetter(TrialSpawnerStateData$Packed::cooldownEndsAt),
            Codec.LONG.lenientOptionalFieldOf("next_mob_spawns_at", 0L).forGetter(TrialSpawnerStateData$Packed::nextMobSpawnsAt),
            Codec.intRange(0, Integer.MAX_VALUE).lenientOptionalFieldOf("total_mobs_spawned", 0).forGetter(TrialSpawnerStateData$Packed::totalMobsSpawned),
            SpawnData.CODEC.lenientOptionalFieldOf("spawn_data").forGetter(TrialSpawnerStateData$Packed::nextSpawnData),
            LootTable.KEY_CODEC.lenientOptionalFieldOf("ejecting_loot_table").forGetter(TrialSpawnerStateData$Packed::ejectingLootTable)
         )
         .apply(i, TrialSpawnerStateData$Packed::new)
   );
}
