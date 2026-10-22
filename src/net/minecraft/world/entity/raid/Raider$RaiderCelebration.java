package net.minecraft.world.entity.raid;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

public class Raider$RaiderCelebration extends Goal {
   private final Raider mob;

   public Raider$RaiderCelebration(final Raider this$0, final Raider mob) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.mob = mob;
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      Raid currentRaid = this.mob.getCurrentRaid();
      return this.mob.isAlive() && this.mob.getTarget() == null && currentRaid != null && currentRaid.isLoss();
   }

   @Override
   public void start() {
      this.mob.setCelebrating(true);
      super.start();
   }

   @Override
   public void stop() {
      this.mob.setCelebrating(false);
      super.stop();
   }

   @Override
   public void tick() {
      if (!this.mob.isSilent() && Raider.access$000(this.mob).nextInt(this.adjustedTickDelay(100)) == 0) {
         this.this$0.makeSound(this.this$0.getCelebrateSound());
      }

      if (!this.mob.isPassenger() && Raider.access$100(this.mob).nextInt(this.adjustedTickDelay(50)) == 0) {
         this.mob.getJumpControl().jump();
      }

      super.tick();
   }
}
