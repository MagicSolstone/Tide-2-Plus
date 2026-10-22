package net.minecraft.world.entity.animal.camel;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;

public class CamelAi$CamelPanic extends AnimalPanic<Camel> {
   public CamelAi$CamelPanic(final float speedMultiplier) {
      super(speedMultiplier);
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Camel body) {
      return super.checkExtraStartConditions(level, body) && !body.isMobControlled();
   }

   protected void start(final ServerLevel level, final Camel camel, final long timestamp) {
      camel.standUpInstantly();
      super.start(level, camel, timestamp);
   }
}
