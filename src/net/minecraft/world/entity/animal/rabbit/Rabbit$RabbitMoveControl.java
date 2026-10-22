package net.minecraft.world.entity.animal.rabbit;

import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;

class Rabbit$RabbitMoveControl<T extends Rabbit> extends MoveControl<T> {
   private double nextJumpSpeed;

   public Rabbit$RabbitMoveControl(final T rabbit) {
      super(rabbit);
   }

   @Override
   public void tick() {
      if (this.mob.onGround() && !Rabbit.access$000(this.mob) && !((Rabbit$RabbitJumpControl)Rabbit.access$100(this.mob)).wantJump()) {
         this.mob.setSpeedModifier(0.0);
      } else if (this.hasWanted() || this.operation == MoveControl$Operation.JUMPING) {
         this.mob.setSpeedModifier(this.nextJumpSpeed);
      }

      super.tick();
   }

   @Override
   public void setWantedPosition(final double x, final double y, final double z, double speedModifier) {
      if (this.mob.isInWater()) {
         speedModifier = 1.5;
      }

      super.setWantedPosition(x, y, z, speedModifier);
      if (speedModifier > 0.0) {
         this.nextJumpSpeed = speedModifier;
      }
   }
}
