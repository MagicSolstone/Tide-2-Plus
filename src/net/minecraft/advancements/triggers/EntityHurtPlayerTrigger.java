package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public class EntityHurtPlayerTrigger extends SimpleCriterionTrigger<EntityHurtPlayerTrigger$TriggerInstance> {
   @Override
   public Codec<EntityHurtPlayerTrigger$TriggerInstance> codec() {
      return EntityHurtPlayerTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final DamageSource source, final float originalDamage, final float actualDamage, final boolean blocked) {
      this.trigger(player, t -> t.matches(player, source, originalDamage, actualDamage, blocked));
   }
}
