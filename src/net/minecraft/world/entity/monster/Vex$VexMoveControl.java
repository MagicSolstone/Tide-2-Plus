package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.MoveControl$Operation;
import net.minecraft.world.phys.Vec3;

class Vex$VexMoveControl<T extends Mob> extends MoveControl<T> {
   public Vex$VexMoveControl(final T this$0, final Mob vex) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(vex);
   }

   @Override
   public void tick() {
      if (this.operation == MoveControl$Operation.MOVE_TO) {
         Vec3 delta = new Vec3(this.wantedX - this.this$0.getX(), this.wantedY - this.this$0.getY(), this.wantedZ - this.this$0.getZ());
         double deltaLength = delta.length();
         if (deltaLength < this.this$0.getBoundingBox().getSize()) {
            this.operation = MoveControl$Operation.WAIT;
            this.this$0.setDeltaMovement(this.this$0.getDeltaMovement().scale(0.5));
         } else {
            this.this$0.setDeltaMovement(this.this$0.getDeltaMovement().add(delta.scale(this.speedModifier * 0.05 / deltaLength)));
            if (this.this$0.getTarget() == null) {
               Vec3 movement = this.this$0.getDeltaMovement();
               this.this$0.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * (180.0F / (float)Math.PI));
               this.this$0.yBodyRot = this.this$0.getYRot();
            } else {
               double tx = this.this$0.getTarget().getX() - this.this$0.getX();
               double tz = this.this$0.getTarget().getZ() - this.this$0.getZ();
               this.this$0.setYRot(-((float)Mth.atan2(tx, tz)) * (180.0F / (float)Math.PI));
               this.this$0.yBodyRot = this.this$0.getYRot();
            }
         }
      }
   }
}
