package net.minecraft.world.entity.animal.camel;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class CamelAi$RandomSitting extends Behavior<Camel> {
   private final int minimalPoseTicks;

   public CamelAi$RandomSitting(final int minimalPoseTimeSec) {
      super(ImmutableMap.of());
      this.minimalPoseTicks = minimalPoseTimeSec * 20;
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Camel body) {
      return !body.isInWater()
         && body.getPoseTime() >= this.minimalPoseTicks
         && !body.isLeashed()
         && body.onGround()
         && !body.hasControllingPassenger()
         && body.canCamelChangePose();
   }

   protected void start(final ServerLevel level, final Camel body, final long timestamp) {
      if (body.isCamelSitting()) {
         body.standUp();
      } else if (!body.isPanicking()) {
         body.sitDown();
      }
   }
}
