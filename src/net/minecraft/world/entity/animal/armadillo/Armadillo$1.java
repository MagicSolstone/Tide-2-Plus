package net.minecraft.world.entity.animal.armadillo;

import java.util.Objects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

class Armadillo$1 extends BodyRotationControl {
   Armadillo$1(final Armadillo this$0, final Mob mob) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(mob);
   }

   @Override
   public void clientTick() {
      if (!this.this$0.isScared()) {
         super.clientTick();
      }
   }
}
