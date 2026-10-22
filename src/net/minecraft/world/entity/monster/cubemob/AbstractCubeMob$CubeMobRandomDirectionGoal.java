package net.minecraft.world.entity.monster.cubemob;

import java.util.EnumSet;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

class AbstractCubeMob$CubeMobRandomDirectionGoal extends Goal {
   private final AbstractCubeMob cubeMob;
   private float chosenDegrees;
   private int nextRandomizeTime;

   public AbstractCubeMob$CubeMobRandomDirectionGoal(final AbstractCubeMob cubeMob) {
      this.cubeMob = cubeMob;
      this.setFlags(EnumSet.of(Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      return this.cubeMob.getTarget() == null
         && (this.cubeMob.onGround() || this.cubeMob.isInLiquid() || this.cubeMob.hasEffect(MobEffects.LEVITATION))
         && this.cubeMob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl;
   }

   @Override
   public void tick() {
      if (--this.nextRandomizeTime <= 0) {
         this.nextRandomizeTime = this.adjustedTickDelay(40 + this.cubeMob.getRandom().nextInt(60));
         this.chosenDegrees = this.cubeMob.getRandom().nextInt(360);
      }

      if (this.cubeMob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl cubeMobMoveControl) {
         cubeMobMoveControl.setDirection(this.chosenDegrees, false);
      }
   }
}
