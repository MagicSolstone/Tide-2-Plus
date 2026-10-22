package net.minecraft.world.entity.animal.fox;

import net.minecraft.world.entity.ai.control.MoveControl;

class Fox$FoxMoveControl<T extends Fox> extends MoveControl<T> {
   public Fox$FoxMoveControl(final T fox) {
      super(fox);
   }

   @Override
   public void tick() {
      if (this.mob.canMove()) {
         super.tick();
      }
   }
}
