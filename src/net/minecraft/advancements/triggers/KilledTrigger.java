package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class KilledTrigger extends SimpleCriterionTrigger<KilledTrigger$TriggerInstance> {
   @Override
   public Codec<KilledTrigger$TriggerInstance> codec() {
      return KilledTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Entity entity, final DamageSource killingBlow) {
      LootContext entityContext = EntityPredicate.createContext(player, entity);
      this.trigger(player, t -> t.matches(player, entityContext, killingBlow));
   }
}
