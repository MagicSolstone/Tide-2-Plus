package net.minecraft.world.entity.animal.camel;

import java.util.Objects;
import net.minecraft.world.entity.ai.control.LookControl;

class Camel$CamelLookControl extends LookControl {
   private Camel$CamelLookControl(final Camel this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public void tick() {
      if (!this.this$0.hasControllingPassenger()) {
         super.tick();
      }
   }
}
