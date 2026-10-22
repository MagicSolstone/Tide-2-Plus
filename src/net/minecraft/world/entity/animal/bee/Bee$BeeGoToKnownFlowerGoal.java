package net.minecraft.world.entity.animal.bee;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

public class Bee$BeeGoToKnownFlowerGoal extends Bee$BaseBeeGoal {
   private static final int MAX_TRAVELLING_TICKS = 2400;
   private int travellingTicks;

   public Bee$BeeGoToKnownFlowerGoal(final Bee this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canBeeUse() {
      return this.this$0.savedFlowerPos != null
         && !this.this$0.hasHome()
         && this.wantsToGoToKnownFlower()
         && !this.this$0.closerThan(this.this$0.savedFlowerPos, 2);
   }

   @Override
   public boolean canBeeContinueToUse() {
      return this.canBeeUse();
   }

   @Override
   public void start() {
      this.travellingTicks = 0;
      super.start();
   }

   @Override
   public void stop() {
      this.travellingTicks = 0;
      Bee.access$1500(this.this$0).stop();
      Bee.access$1600(this.this$0).resetMaxVisitedNodesMultiplier();
   }

   @Override
   public void tick() {
      if (this.this$0.savedFlowerPos != null) {
         this.travellingTicks++;
         if (this.travellingTicks > this.adjustedTickDelay(2400)) {
            this.this$0.dropFlower();
         } else if (!Bee.access$1700(this.this$0).isInProgress()) {
            if (this.this$0.isTooFarAway(this.this$0.savedFlowerPos)) {
               this.this$0.dropFlower();
            } else {
               this.this$0.pathfindRandomlyTowards(this.this$0.savedFlowerPos);
            }
         }
      }
   }

   private boolean wantsToGoToKnownFlower() {
      return this.this$0.ticksWithoutNectarSinceExitingHive > 600;
   }
}
