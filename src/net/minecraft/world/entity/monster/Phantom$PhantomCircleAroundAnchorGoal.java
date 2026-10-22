package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

class Phantom$PhantomCircleAroundAnchorGoal extends Phantom$PhantomMoveTargetGoal {
   private float angle;
   private float distance;
   private float height;
   private float clockwise;

   private Phantom$PhantomCircleAroundAnchorGoal(final Phantom this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canUse() {
      return this.this$0.getTarget() == null || this.this$0.attackPhase == Phantom$AttackPhase.CIRCLE;
   }

   @Override
   public void start() {
      this.distance = 5.0F + Phantom.access$000(this.this$0).nextFloat() * 10.0F;
      this.height = -4.0F + Phantom.access$100(this.this$0).nextFloat() * 9.0F;
      this.clockwise = Phantom.access$200(this.this$0).nextBoolean() ? 1.0F : -1.0F;
      this.selectNext();
   }

   @Override
   public void tick() {
      if (Phantom.access$300(this.this$0).nextInt(this.adjustedTickDelay(350)) == 0) {
         this.height = -4.0F + Phantom.access$400(this.this$0).nextFloat() * 9.0F;
      }

      if (Phantom.access$500(this.this$0).nextInt(this.adjustedTickDelay(250)) == 0) {
         this.distance++;
         if (this.distance > 15.0F) {
            this.distance = 5.0F;
            this.clockwise = -this.clockwise;
         }
      }

      if (Phantom.access$600(this.this$0).nextInt(this.adjustedTickDelay(450)) == 0) {
         this.angle = Phantom.access$700(this.this$0).nextFloat() * 2.0F * (float) Math.PI;
         this.selectNext();
      }

      if (this.touchingTarget()) {
         this.selectNext();
      }

      if (this.this$0.moveTargetPoint.y < this.this$0.getY() && !this.this$0.level().isEmptyBlock(this.this$0.blockPosition().below(1))) {
         this.height = Math.max(1.0F, this.height);
         this.selectNext();
      }

      if (this.this$0.moveTargetPoint.y > this.this$0.getY() && !this.this$0.level().isEmptyBlock(this.this$0.blockPosition().above(1))) {
         this.height = Math.min(-1.0F, this.height);
         this.selectNext();
      }
   }

   private void selectNext() {
      if (this.this$0.anchorPoint == null) {
         this.this$0.anchorPoint = this.this$0.blockPosition();
      }

      this.angle = this.angle + this.clockwise * 15.0F * (float) (Math.PI / 180.0);
      this.this$0.moveTargetPoint = Vec3.atLowerCornerOf(this.this$0.anchorPoint)
         .add(this.distance * Mth.cos(this.angle), -4.0F + this.height, this.distance * Mth.sin(this.angle));
   }
}
