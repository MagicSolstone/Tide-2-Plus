package net.minecraft.world.entity.monster.creaking;

import net.minecraft.world.entity.ai.control.MoveControl;

class Creaking$CreakingMoveControl<T extends Creaking> extends MoveControl<T> {
   public Creaking$CreakingMoveControl(final T creaking) {
      super(creaking);
   }

   @Override
   public void tick() {
      if (this.mob.canMove()) {
         super.tick();
      }
   }
}
