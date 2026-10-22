package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class LevitationTrigger extends SimpleCriterionTrigger<LevitationTrigger$TriggerInstance> {
   @Override
   public Codec<LevitationTrigger$TriggerInstance> codec() {
      return LevitationTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Vec3 start, final int duration) {
      this.trigger(player, t -> t.matches(player, start, duration));
   }
}
