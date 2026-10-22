package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;

public class PlayerTrigger extends SimpleCriterionTrigger<PlayerTrigger$TriggerInstance> {
   @Override
   public Codec<PlayerTrigger$TriggerInstance> codec() {
      return PlayerTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player) {
      this.trigger(player, t -> true);
   }
}
