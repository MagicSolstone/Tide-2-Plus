package net.minecraft.world.level.block.entity.trialspawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.TypedEntityData;

public record TrialSpawner$FullConfig(Holder<TrialSpawnerConfig> normal, Holder<TrialSpawnerConfig> ominous, int targetCooldownLength, int requiredPlayerRange) {
   public static final MapCodec<TrialSpawner$FullConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            TrialSpawnerConfig.CODEC.optionalFieldOf("normal_config", Holder.direct(TrialSpawnerConfig.DEFAULT)).forGetter(TrialSpawner$FullConfig::normal),
            TrialSpawnerConfig.CODEC.optionalFieldOf("ominous_config", Holder.direct(TrialSpawnerConfig.DEFAULT)).forGetter(TrialSpawner$FullConfig::ominous),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("target_cooldown_length", 36000).forGetter(TrialSpawner$FullConfig::targetCooldownLength),
            Codec.intRange(1, 128).optionalFieldOf("required_player_range", 14).forGetter(TrialSpawner$FullConfig::requiredPlayerRange)
         )
         .apply(i, TrialSpawner$FullConfig::new)
   );
   public static final TrialSpawner$FullConfig DEFAULT = new TrialSpawner$FullConfig(
      Holder.direct(TrialSpawnerConfig.DEFAULT), Holder.direct(TrialSpawnerConfig.DEFAULT), 36000, 14
   );

   public TrialSpawner$FullConfig overrideEntity(final EntityType<?> type) {
      return new TrialSpawner$FullConfig(
         Holder.direct(this.normal.value().withSpawning(type)),
         Holder.direct(this.ominous.value().withSpawning(type)),
         this.targetCooldownLength,
         this.requiredPlayerRange
      );
   }

   public TrialSpawner$FullConfig overrideEntityData(final TypedEntityData<EntityType<?>> entityData) {
      return new TrialSpawner$FullConfig(
         Holder.direct(this.normal.value().withSpawning(entityData)),
         Holder.direct(this.ominous.value().withSpawning(entityData)),
         this.targetCooldownLength,
         this.requiredPlayerRange
      );
   }
}
