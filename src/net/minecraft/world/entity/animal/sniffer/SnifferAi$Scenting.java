package net.minecraft.world.entity.animal.sniffer;

import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

class SnifferAi$Scenting extends Behavior<Sniffer> {
   private SnifferAi$Scenting(final int min, final int max) {
      super(
         Map.of(
            MemoryModuleType.IS_PANICKING,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFFER_DIGGING,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFFER_SNIFFING_TARGET,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFFER_HAPPY,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.BREED_TARGET,
            MemoryStatus.VALUE_ABSENT
         ),
         min,
         max
      );
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Sniffer sniffer) {
      return !sniffer.isTempted();
   }

   protected boolean canStillUse(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      return true;
   }

   protected void start(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.SCENTING);
   }

   protected void stop(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.IDLING);
   }
}
