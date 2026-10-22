package net.minecraft.world.entity.animal.sniffer;

import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

class SnifferAi$FinishedDigging extends Behavior<Sniffer> {
   private SnifferAi$FinishedDigging(final int duration) {
      super(
         Map.of(
            MemoryModuleType.IS_PANICKING,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.WALK_TARGET,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFFER_DIGGING,
            MemoryStatus.VALUE_PRESENT,
            MemoryModuleType.SNIFF_COOLDOWN,
            MemoryStatus.VALUE_PRESENT
         ),
         duration,
         duration
      );
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Sniffer sniffer) {
      return true;
   }

   protected boolean canStillUse(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      return sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent();
   }

   protected void start(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.RISING);
   }

   protected void stop(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      boolean finished = this.timedOut(timestamp);
      sniffer.transitionTo(Sniffer$State.IDLING).onDiggingComplete(finished);
      sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
      sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_HAPPY, true);
   }
}
