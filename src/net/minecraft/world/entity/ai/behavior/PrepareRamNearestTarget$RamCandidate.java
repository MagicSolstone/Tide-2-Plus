package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public class PrepareRamNearestTarget$RamCandidate {
   private final BlockPos startPosition;
   private final BlockPos targetPosition;
   private final LivingEntity target;

   public PrepareRamNearestTarget$RamCandidate(final BlockPos startPosition, final BlockPos targetPosition, final LivingEntity target) {
      this.startPosition = startPosition;
      this.targetPosition = targetPosition;
      this.target = target;
   }

   public BlockPos getStartPosition() {
      return this.startPosition;
   }

   public BlockPos getTargetPosition() {
      return this.targetPosition;
   }

   public LivingEntity getTarget() {
      return this.target;
   }
}
