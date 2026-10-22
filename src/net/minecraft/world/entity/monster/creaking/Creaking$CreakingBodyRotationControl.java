package net.minecraft.world.entity.monster.creaking;

import java.util.Objects;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

class Creaking$CreakingBodyRotationControl extends BodyRotationControl {
   public Creaking$CreakingBodyRotationControl(final Creaking this$0, final Creaking creaking) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(creaking);
   }

   @Override
   public void clientTick() {
      if (this.this$0.canMove()) {
         super.clientTick();
      }
   }
}
