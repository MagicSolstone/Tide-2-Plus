package net.minecraft.world.entity.animal.sniffer;

import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

class SnifferAi$Searching extends Behavior<Sniffer> {
   private SnifferAi$Searching() {
      super(
         Map.of(
            MemoryModuleType.WALK_TARGET,
            MemoryStatus.VALUE_PRESENT,
            MemoryModuleType.IS_PANICKING,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFFER_SNIFFING_TARGET,
            MemoryStatus.VALUE_PRESENT
         ),
         600
      );
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Sniffer sniffer) {
      return sniffer.canSniff();
   }

   protected boolean canStillUse(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      if (!sniffer.canSniff()) {
         sniffer.transitionTo(Sniffer$State.IDLING);
         return false;
      } else {
         Optional<BlockPos> walkTarget = sniffer.getBrain()
            .getMemory(MemoryModuleType.WALK_TARGET)
            .map(WalkTarget::getTarget)
            .map(PositionTracker::currentBlockPosition);
         Optional<BlockPos> sniffingTarget = sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
         return !walkTarget.isEmpty() && !sniffingTarget.isEmpty() ? sniffingTarget.get().equals(walkTarget.get()) : false;
      }
   }

   protected void start(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.SEARCHING);
   }

   protected void stop(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      if (sniffer.canDig() && sniffer.canSniff()) {
         sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_DIGGING, true);
      }

      sniffer.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
      sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
   }
}
