package net.minecraft.world.entity.animal.sniffer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.animal.Animal;

class SnifferAi$2 extends AnimalMakeLove {
   SnifferAi$2(final EntityType partnerType) {
      super(partnerType);
   }

   @Override
   protected void start(final ServerLevel level, final Animal body, final long timestamp) {
      SnifferAi.resetSniffing((Sniffer)body);
      super.start(level, body, timestamp);
   }
}
