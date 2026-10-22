package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;

public class StartRidingTrigger extends SimpleCriterionTrigger<StartRidingTrigger$TriggerInstance> {
   @Override
   public Codec<StartRidingTrigger$TriggerInstance> codec() {
      return StartRidingTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player) {
      this.trigger(player, t -> true);
   }
}
