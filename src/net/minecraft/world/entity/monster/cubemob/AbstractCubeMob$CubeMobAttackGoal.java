package net.minecraft.world.entity.monster.cubemob;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

public class AbstractCubeMob$CubeMobAttackGoal extends Goal {
   private final AbstractCubeMob cubeMob;
   private int growTiredTimer;

   public AbstractCubeMob$CubeMobAttackGoal(final AbstractCubeMob cubeMob) {
      this.cubeMob = cubeMob;
      this.setFlags(EnumSet.of(Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      LivingEntity target = this.cubeMob.getTarget();
      if (target == null) {
         return false;
      } else {
         return !this.cubeMob.canAttack(target) ? false : this.cubeMob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl;
      }
   }

   @Override
   public void start() {
      this.growTiredTimer = reducedTickDelay(300);
      super.start();
   }

   @Override
   public boolean canContinueToUse() {
      LivingEntity target = this.cubeMob.getTarget();
      if (target == null) {
         return false;
      } else {
         return !this.cubeMob.canAttack(target) ? false : --this.growTiredTimer > 0;
      }
   }

   @Override
   public boolean requiresUpdateEveryTick() {
      return true;
   }

   @Override
   public void tick() {
      LivingEntity target = this.cubeMob.getTarget();
      if (target != null) {
         this.cubeMob.lookAt(target, 10.0F, 10.0F);
      }

      if (this.cubeMob.getMoveControl() instanceof AbstractCubeMob$CubeMobMoveControl cubeMobMoveControl) {
         cubeMobMoveControl.setDirection(this.cubeMob.getYRot(), this.cubeMob.canDealDamage());
      }
   }
}
