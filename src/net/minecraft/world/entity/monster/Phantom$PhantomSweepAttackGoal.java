package net.minecraft.world.entity.monster;

import java.util.List;
import java.util.Objects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

class Phantom$PhantomSweepAttackGoal extends Phantom$PhantomMoveTargetGoal {
   private static final int CAT_SEARCH_TICK_DELAY = 20;
   private boolean isScaredOfCat;
   private int catSearchTick;

   private Phantom$PhantomSweepAttackGoal(final Phantom this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canUse() {
      return this.this$0.getTarget() != null && this.this$0.attackPhase == Phantom$AttackPhase.SWOOP;
   }

   @Override
   public boolean canContinueToUse() {
      LivingEntity target = this.this$0.getTarget();
      if (target == null) {
         return false;
      } else if (!target.isAlive()) {
         return false;
      } else if (target instanceof Player player && (target.isSpectator() || player.isCreative())) {
         return false;
      } else if (!this.canUse()) {
         return false;
      } else {
         if (this.this$0.tickCount > this.catSearchTick) {
            this.catSearchTick = this.this$0.tickCount + 20;
            List<Cat> cats = this.this$0.level().getEntitiesOfClass(Cat.class, this.this$0.getBoundingBox().inflate(16.0), EntitySelector.ENTITY_STILL_ALIVE);

            for (Cat cat : cats) {
               cat.hiss();
            }

            this.isScaredOfCat = !cats.isEmpty();
         }

         return !this.isScaredOfCat;
      }
   }

   @Override
   public void stop() {
      this.this$0.setTarget(null);
      this.this$0.attackPhase = Phantom$AttackPhase.CIRCLE;
   }

   @Override
   public void tick() {
      LivingEntity target = this.this$0.getTarget();
      if (target != null) {
         this.this$0.moveTargetPoint = new Vec3(target.getX(), target.getY(0.5), target.getZ());
         if (this.this$0.getBoundingBox().inflate(0.2F).intersects(target.getBoundingBox())) {
            this.this$0.doHurtTarget(getServerLevel(this.this$0.level()), target);
            this.this$0.attackPhase = Phantom$AttackPhase.CIRCLE;
            if (!this.this$0.isSilent()) {
               this.this$0.level().levelEvent(1039, this.this$0.blockPosition(), 0);
            }
         } else if (this.this$0.horizontalCollision || this.this$0.wasHurtRecently()) {
            this.this$0.attackPhase = Phantom$AttackPhase.CIRCLE;
         }
      }
   }
}
