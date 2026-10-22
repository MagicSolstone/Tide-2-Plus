package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class Vex$VexRandomMoveGoal extends Goal {
   public Vex$VexRandomMoveGoal(final Vex this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      return !this.this$0.getMoveControl().hasWanted() && Vex.access$300(this.this$0).nextInt(reducedTickDelay(7)) == 0;
   }

   @Override
   public boolean canContinueToUse() {
      return false;
   }

   @Override
   public void tick() {
      BlockPos boundOrigin = this.this$0.getBoundOrigin();
      if (boundOrigin == null) {
         boundOrigin = this.this$0.blockPosition();
      }

      for (int attempts = 0; attempts < 3; attempts++) {
         BlockPos testPos = boundOrigin.offset(
            Vex.access$400(this.this$0).nextInt(15) - 7, Vex.access$500(this.this$0).nextInt(11) - 5, Vex.access$600(this.this$0).nextInt(15) - 7
         );
         if (this.this$0.level().isEmptyBlock(testPos)) {
            Vex.access$700(this.this$0).setWantedPosition(testPos.getX() + 0.5, testPos.getY() + 0.5, testPos.getZ() + 0.5, 0.25);
            if (this.this$0.getTarget() == null) {
               this.this$0.getLookControl().setLookAt(testPos.getX() + 0.5, testPos.getY() + 0.5, testPos.getZ() + 0.5, 180.0F, 20.0F);
            }
            break;
         }
      }
   }
}
