package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

public record TargetBlockTrigger$TriggerInstance(
   Optional<Holder<LootItemCondition>> player, MinMaxBounds$Ints signalStrength, Optional<Holder<LootItemCondition>> projectile
) implements SimpleCriterionTrigger$SimpleInstance {
   public static final Codec<TargetBlockTrigger$TriggerInstance> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TargetBlockTrigger$TriggerInstance::player),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("signal_strength", MinMaxBounds$Ints.ANY).forGetter(TargetBlockTrigger$TriggerInstance::signalStrength),
            LootItemCondition.CODEC.optionalFieldOf("projectile").forGetter(TargetBlockTrigger$TriggerInstance::projectile)
         )
         .apply(i, TargetBlockTrigger$TriggerInstance::new)
   );

   public static Criterion<TargetBlockTrigger$TriggerInstance> targetHit(
      final MinMaxBounds$Ints redstoneSignalStrength, final Optional<Holder<LootItemCondition>> projectile
   ) {
      return CriteriaTriggers.TARGET_BLOCK_HIT.createCriterion(new TargetBlockTrigger$TriggerInstance(Optional.empty(), redstoneSignalStrength, projectile));
   }

   public boolean matches(final LootContext projectile, final Vec3 hitPosition, final int signalStrength) {
      return !this.signalStrength.matches(signalStrength) ? false : !this.projectile.isPresent() || this.projectile.get().value().test(projectile);
   }

   @Override
   public void validate(final ValidationContextSource validator) {
      SimpleCriterionTrigger$SimpleInstance.super.validate(validator);
      Validatable.validateHolder(validator.entityContext(), "projectile", this.projectile);
   }
}
