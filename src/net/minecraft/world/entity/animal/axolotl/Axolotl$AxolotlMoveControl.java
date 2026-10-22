package net.minecraft.world.entity.animal.axolotl;

import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;

class Axolotl$AxolotlMoveControl<T extends Axolotl> extends SmoothSwimmingMoveControl<T> {
   public Axolotl$AxolotlMoveControl(final T axolotl) {
      super(axolotl, 85, 10, 0.1F, 0.5F, false);
   }

   @Override
   public void tick() {
      if (!this.mob.isPlayingDead()) {
         super.tick();
      }
   }
}
