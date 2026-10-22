package net.minecraft.world.entity.animal.happyghast;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.monster.Ghast;

class HappyGhast$HappyGhastLookControl extends LookControl {
   private HappyGhast$HappyGhastLookControl(final HappyGhast this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public void tick() {
      if (this.this$0.isOnStillTimeout()) {
         float closeAngle = Mth.wrapDegrees90(this.this$0.getYRot());
         this.this$0.setYRot(this.this$0.getYRot() - closeAngle);
         this.this$0.setYHeadRot(this.this$0.getYRot());
      } else if (this.lookAtCooldown > 0) {
         this.lookAtCooldown--;
         double xdd = this.wantedX - this.this$0.getX();
         double zdd = this.wantedZ - this.this$0.getZ();
         this.this$0.setYRot(-((float)Mth.atan2(xdd, zdd)) * (180.0F / (float)Math.PI));
         this.this$0.yBodyRot = this.this$0.getYRot();
         this.this$0.yHeadRot = this.this$0.yBodyRot;
      } else {
         Ghast.faceMovementDirection(this.mob);
      }
   }
}
