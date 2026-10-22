package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class UsedEnderEyeTrigger extends SimpleCriterionTrigger<UsedEnderEyeTrigger$TriggerInstance> {
   @Override
   public Codec<UsedEnderEyeTrigger$TriggerInstance> codec() {
      return UsedEnderEyeTrigger$TriggerInstance.CODEC;
   }

   public void trigger(final ServerPlayer player, final BlockPos feature) {
      double xd = player.getX() - feature.getX();
      double zd = player.getZ() - feature.getZ();
      double sqrDist = xd * xd + zd * zd;
      this.trigger(player, t -> t.matches(sqrDist));
   }
}
