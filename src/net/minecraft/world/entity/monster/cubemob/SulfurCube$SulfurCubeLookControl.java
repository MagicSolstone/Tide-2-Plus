package net.minecraft.world.entity.monster.cubemob;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.LookControl;

class SulfurCube$SulfurCubeLookControl extends LookControl {
   private SulfurCube$SulfurCubeLookControl(final SulfurCube this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public void tick() {
      if (!this.this$0.hasBodyItem()) {
         super.tick();
      } else {
         float closeAngle = Mth.wrapDegrees90(this.this$0.getYRot());
         this.this$0.setYRot(this.this$0.getYRot() - closeAngle);
         this.this$0.setYHeadRot(this.this$0.getYRot());
      }
   }
}
