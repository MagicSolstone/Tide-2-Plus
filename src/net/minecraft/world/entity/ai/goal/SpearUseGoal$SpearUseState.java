package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SpearUseGoal$SpearUseState {
   private int engageTime = -1;
   private int fleeingTime = -1;
   @Nullable
   private Vec3 awayPos;
   private boolean done = false;

   public boolean notEngagedYet() {
      return this.engageTime < 0;
   }

   public void startEngagement(final int spearDownTime) {
      this.engageTime = spearDownTime;
   }

   public boolean tickAndCheckEngagement() {
      if (this.engageTime > 0) {
         this.engageTime--;
         if (this.engageTime == 0) {
            return true;
         }
      }

      return false;
   }

   public boolean tickAndCheckFleeing() {
      if (this.fleeingTime > 0) {
         this.fleeingTime++;
         if (this.fleeingTime > SpearUseGoal.MAX_FLEEING_TIME) {
            this.done = true;
            return true;
         }
      }

      return false;
   }
}
