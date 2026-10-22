package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;

class Fox$SeekShelterGoal extends FleeSunGoal {
   private int interval;

   public Fox$SeekShelterGoal(final Fox this$0, final double speedModifier) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, speedModifier);
      this.interval = reducedTickDelay(100);
   }

   @Override
   public boolean canUse() {
      if (!this.this$0.isSleeping() && this.mob.getTarget() == null) {
         if (this.this$0.level().isThundering() && this.this$0.level().canSeeSky(this.mob.blockPosition())) {
            return this.setWantedPos();
         } else if (this.interval > 0) {
            this.interval--;
            return false;
         } else {
            this.interval = 100;
            BlockPos pos = this.mob.blockPosition();
            return this.this$0.level().isBrightOutside()
               && this.this$0.level().canSeeSky(pos)
               && !((ServerLevel)this.this$0.level()).isVillage(pos)
               && this.setWantedPos();
         }
      } else {
         return false;
      }
   }

   @Override
   public void start() {
      this.this$0.clearStates();
      super.start();
   }
}
