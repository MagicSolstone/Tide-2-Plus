package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;

public class ConstructBeaconTrigger extends SimpleCriterionTrigger<ConstructBeaconTrigger$TriggerInstance> {
   @Override
   public Codec<ConstructBeaconTrigger$TriggerInstance> codec() {
      return ConstructBeaconTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final int levels) {
      this.trigger(player, t -> t.matches(levels));
   }
}
