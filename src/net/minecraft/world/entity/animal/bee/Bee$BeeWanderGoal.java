package net.minecraft.world.entity.animal.bee;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

class Bee$BeeWanderGoal extends Goal {
   public Bee$BeeWanderGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      return Bee.access$000(this.this$0).isDone() && Bee.access$100(this.this$0).nextInt(10) == 0;
   }

   @Override
   public boolean canContinueToUse() {
      return Bee.access$200(this.this$0).isInProgress();
   }

   @Override
   public void start() {
      Vec3 targetPos = this.findPos();
      if (targetPos != null) {
         Bee.access$400(this.this$0).moveTo(Bee.access$300(this.this$0).createPath(BlockPos.containing(targetPos), 1), 1.0);
      }
   }

   @Nullable
   private Vec3 findPos() {
      Vec3 wanderDirection;
      if (this.this$0.isHiveValid() && !this.this$0.closerThan(this.this$0.hivePos, this.getWanderThreshold())) {
         Vec3 hivePosVec = Vec3.atCenterOf(this.this$0.hivePos);
         wanderDirection = hivePosVec.subtract(this.this$0.position()).normalize();
      } else {
         wanderDirection = this.this$0.getViewVector(0.0F);
      }

      int xzDist = 8;
      Vec3 groundBasedPosition = HoverRandomPos.getPos(this.this$0, 8, 7, wanderDirection.x, wanderDirection.z, (float) (Math.PI / 2), 3, 1);
      return groundBasedPosition != null
         ? groundBasedPosition
         : AirAndWaterRandomPos.getPos(this.this$0, 8, 4, -2, wanderDirection.x, wanderDirection.z, (float) (Math.PI / 2));
   }

   private int getWanderThreshold() {
      int distanceReduction = !this.this$0.hasHive() && !this.this$0.hasSavedFlowerPos() ? 16 : 24;
      return 48 - distanceReduction;
   }
}
