package net.minecraft.world.entity.monster.creaking;

import java.util.Objects;
import net.minecraft.world.entity.ai.control.LookControl;

class Creaking$CreakingLookControl extends LookControl {
   public Creaking$CreakingLookControl(final Creaking this$0, final Creaking creaking) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(creaking);
   }

   @Override
   public void tick() {
      if (this.this$0.canMove()) {
         super.tick();
      }
   }
}
