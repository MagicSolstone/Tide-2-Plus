package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;

public class SpearMobsTrigger extends SimpleCriterionTrigger<SpearMobsTrigger$TriggerInstance> {
   @Override
   public Codec<SpearMobsTrigger$TriggerInstance> codec() {
      return SpearMobsTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final int number) {
      this.trigger(player, t -> t.matches(number));
   }
}
