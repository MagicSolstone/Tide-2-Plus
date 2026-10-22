package net.minecraft.world.entity.animal.camel;

import java.util.Objects;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

class Camel$CamelBodyRotationControl extends BodyRotationControl {
   public Camel$CamelBodyRotationControl(final Camel this$0, final Camel camel) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(camel);
   }

   @Override
   public void clientTick() {
      if (!this.this$0.refuseToMove()) {
         super.clientTick();
      }
   }
}
