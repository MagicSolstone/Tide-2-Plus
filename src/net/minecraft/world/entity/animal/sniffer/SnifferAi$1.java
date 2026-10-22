package net.minecraft.world.entity.animal.sniffer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;

class SnifferAi$1 extends AnimalPanic<Sniffer> {
   SnifferAi$1(final float speedMultiplier) {
      super(speedMultiplier);
   }

   protected void start(final ServerLevel level, final Sniffer body, final long timestamp) {
      SnifferAi.resetSniffing(body);
      super.start(level, body, timestamp);
   }
}
