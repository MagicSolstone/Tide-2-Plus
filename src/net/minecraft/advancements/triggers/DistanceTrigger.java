package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class DistanceTrigger extends SimpleCriterionTrigger<DistanceTrigger$TriggerInstance> {
   @Override
   public Codec<DistanceTrigger$TriggerInstance> codec() {
      return DistanceTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final Vec3 startPosition) {
      Vec3 playerPosition = player.position();
      this.trigger(player, t -> t.matches(player.level(), startPosition, playerPosition));
   }
}
