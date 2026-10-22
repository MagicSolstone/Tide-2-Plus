package net.minecraft.world.entity.animal.happyghast;

import java.util.Objects;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

class HappyGhast$HappyGhastBodyRotationControl extends BodyRotationControl {
   public HappyGhast$HappyGhastBodyRotationControl(final HappyGhast this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public void clientTick() {
      if (this.this$0.isVehicle()) {
         this.this$0.yHeadRot = this.this$0.getYRot();
         this.this$0.yBodyRot = this.this$0.yHeadRot;
      }

      super.clientTick();
   }
}
