package net.minecraft.world.entity.animal.camel;

import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;

class Camel$CamelMoveControl<T extends Camel> extends MoveControl<T> {
   public Camel$CamelMoveControl(final T camel) {
      super(camel);
   }

   @Override
   public void tick() {
      if (this.operation == MoveControl$Operation.MOVE_TO
         && !this.mob.isLeashed()
         && this.mob.isCamelSitting()
         && !this.mob.isInPoseTransition()
         && this.mob.canCamelChangePose()) {
         this.mob.standUp();
      }

      super.tick();
   }
}
