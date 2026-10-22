package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

public class TargetBlockTrigger extends SimpleCriterionTrigger<TargetBlockTrigger$TriggerInstance> {
   @Override
   public Codec<TargetBlockTrigger$TriggerInstance> codec() {
      return TargetBlockTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Entity projectile, final Vec3 hitPosition, final int signalStrength) {
      LootContext projectileContext = EntityPredicate.createContext(player, projectile);
      this.trigger(player, t -> t.matches(projectileContext, hitPosition, signalStrength));
   }
}
