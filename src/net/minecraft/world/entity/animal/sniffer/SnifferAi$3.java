package net.minecraft.world.entity.animal.sniffer;

import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;

class SnifferAi$3 extends FollowTemptation {
   SnifferAi$3(final Function speedModifier, final Function closeEnoughDistance) {
      super(speedModifier, closeEnoughDistance);
   }

   protected void start(final ServerLevel level, final PathfinderMob body, final long timestamp) {
      SnifferAi.resetSniffing((Sniffer)body);
      super.start(level, body, timestamp);
   }
}
