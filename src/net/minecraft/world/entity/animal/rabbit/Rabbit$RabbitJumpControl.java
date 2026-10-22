package net.minecraft.world.entity.animal.rabbit;

import net.minecraft.world.entity.ai.control.JumpControl;

public class Rabbit$RabbitJumpControl extends JumpControl {
   private final Rabbit rabbit;
   private boolean canJump;

   public Rabbit$RabbitJumpControl(final Rabbit rabbit) {
      super(rabbit);
      this.rabbit = rabbit;
   }

   public boolean wantJump() {
      return this.jump;
   }

   public boolean canJump() {
      return this.canJump;
   }

   public void setCanJump(final boolean canJump) {
      this.canJump = canJump;
   }

   @Override
   public void tick() {
      if (this.jump) {
         this.rabbit.startJumping();
         this.jump = false;
      }
   }
}
