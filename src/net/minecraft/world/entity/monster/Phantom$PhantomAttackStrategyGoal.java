package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.levelgen.Heightmap$Types;

class Phantom$PhantomAttackStrategyGoal extends Goal {
   private int nextSweepTick;

   private Phantom$PhantomAttackStrategyGoal(final Phantom this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   @Override
   public boolean canUse() {
      LivingEntity target = this.this$0.getTarget();
      return target != null ? this.this$0.canAttack(getServerLevel(this.this$0.level()), target, TargetingConditions.DEFAULT) : false;
   }

   @Override
   public void start() {
      this.nextSweepTick = this.adjustedTickDelay(10);
      this.this$0.attackPhase = Phantom$AttackPhase.CIRCLE;
      this.setAnchorAboveTarget();
   }

   @Override
   public void stop() {
      if (this.this$0.anchorPoint != null) {
         this.this$0.anchorPoint = this.this$0
            .level()
            .getHeightmapPos(Heightmap$Types.MOTION_BLOCKING, this.this$0.anchorPoint)
            .above(10 + Phantom.access$800(this.this$0).nextInt(20));
      }
   }

   @Override
   public void tick() {
      if (this.this$0.attackPhase == Phantom$AttackPhase.CIRCLE) {
         this.nextSweepTick--;
         if (this.nextSweepTick <= 0) {
            this.this$0.attackPhase = Phantom$AttackPhase.SWOOP;
            this.setAnchorAboveTarget();
            this.nextSweepTick = this.adjustedTickDelay((8 + Phantom.access$900(this.this$0).nextInt(4)) * 20);
            this.this$0.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + Phantom.access$1000(this.this$0).nextFloat() * 0.1F);
         }
      }
   }

   private void setAnchorAboveTarget() {
      if (this.this$0.anchorPoint != null) {
         this.this$0.anchorPoint = this.this$0.getTarget().blockPosition().above(20 + Phantom.access$1100(this.this$0).nextInt(20));
         if (this.this$0.anchorPoint.getY() < this.this$0.level().getSeaLevel()) {
            this.this$0.anchorPoint = new BlockPos(this.this$0.anchorPoint.getX(), this.this$0.level().getSeaLevel() + 1, this.this$0.anchorPoint.getZ());
         }
      }
   }
}
