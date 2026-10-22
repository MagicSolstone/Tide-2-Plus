package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerHurtEntityTrigger extends SimpleCriterionTrigger<PlayerHurtEntityTrigger$TriggerInstance> {
   @Override
   public Codec<PlayerHurtEntityTrigger$TriggerInstance> codec() {
      return PlayerHurtEntityTrigger$TriggerInstance.CODEC;
   }

   public void trigger(
      final ServerPlayer player, final Entity victim, final DamageSource source, final float originalDamage, final float actualDamage, final boolean blocked
   ) {
      LootContext victimContext = EntityPredicate.createContext(player, victim);
      this.trigger(player, t -> t.matches(player, victimContext, source, originalDamage, actualDamage, blocked));
   }
}
