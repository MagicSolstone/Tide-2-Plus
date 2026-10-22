package net.minecraft.world.entity.animal.panda;

import net.minecraft.world.entity.ai.control.MoveControl;

class Panda$PandaMoveControl<T extends Panda> extends MoveControl<T> {
   public Panda$PandaMoveControl(final T mob) {
      super(mob);
   }

   @Override
   public void tick() {
      if (this.mob.canPerformAction()) {
         super.tick();
      }
   }
}
