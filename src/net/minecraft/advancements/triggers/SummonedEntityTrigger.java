package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class SummonedEntityTrigger extends SimpleCriterionTrigger<SummonedEntityTrigger$TriggerInstance> {
   @Override
   public Codec<SummonedEntityTrigger$TriggerInstance> codec() {
      return SummonedEntityTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Entity entity) {
      LootContext context = EntityPredicate.createContext(player, entity);
      this.trigger(player, t -> t.matches(context));
   }
}
