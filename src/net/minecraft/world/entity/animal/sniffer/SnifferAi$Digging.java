package net.minecraft.world.entity.animal.sniffer;

import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

class SnifferAi$Digging extends Behavior<Sniffer> {
   private SnifferAi$Digging(final int min, final int max) {
      super(
         Map.of(
            MemoryModuleType.IS_PANICKING,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.WALK_TARGET,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFFER_DIGGING,
            MemoryStatus.VALUE_PRESENT,
            MemoryModuleType.SNIFF_COOLDOWN,
            MemoryStatus.VALUE_ABSENT
         ),
         min,
         max
      );
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Sniffer sniffer) {
      return sniffer.canSniff();
   }

   protected boolean canStillUse(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      return sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent() && sniffer.canDig() && !sniffer.isInLove();
   }

   protected void start(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.DIGGING);
   }

   protected void stop(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      boolean finished = this.timedOut(timestamp);
      if (finished) {
         sniffer.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 9600L);
      } else {
         SnifferAi.resetSniffing(sniffer);
      }
   }
}
