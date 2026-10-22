package net.minecraft.world.entity.animal.sniffer;

import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

class SnifferAi$FeelingHappy extends Behavior<Sniffer> {
   private SnifferAi$FeelingHappy(final int min, final int max) {
      super(Map.of(MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_PRESENT), min, max);
   }

   protected boolean canStillUse(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      return true;
   }

   protected void start(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.FEELING_HAPPY);
   }

   protected void stop(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.IDLING);
      sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_HAPPY);
   }
}
