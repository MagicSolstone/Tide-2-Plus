package net.minecraft.world.entity.animal.sniffer;

import java.util.Map;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

class SnifferAi$Sniffing extends Behavior<Sniffer> {
   private SnifferAi$Sniffing(final int min, final int max) {
      super(
         Map.of(
            MemoryModuleType.WALK_TARGET,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFFER_SNIFFING_TARGET,
            MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.SNIFF_COOLDOWN,
            MemoryStatus.VALUE_ABSENT
         ),
         min,
         max
      );
   }

   protected boolean checkExtraStartConditions(final ServerLevel level, final Sniffer body) {
      return !body.isBaby() && body.canSniff();
   }

   protected boolean canStillUse(final ServerLevel level, final Sniffer body, final long timestamp) {
      return body.canSniff();
   }

   protected void start(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      sniffer.transitionTo(Sniffer$State.SNIFFING);
   }

   protected void stop(final ServerLevel level, final Sniffer sniffer, final long timestamp) {
      boolean finished = this.timedOut(timestamp);
      sniffer.transitionTo(Sniffer$State.IDLING);
      if (finished) {
         sniffer.calculateDigPosition().ifPresent(position -> {
            sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET, position);
            sniffer.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(position, 1.25F, 0));
         });
      }
   }
}
