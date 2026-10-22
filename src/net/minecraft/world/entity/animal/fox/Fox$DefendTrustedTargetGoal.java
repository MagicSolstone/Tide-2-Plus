package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions$Selector;
import org.jspecify.annotations.Nullable;

class Fox$DefendTrustedTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
   @Nullable
   private LivingEntity trustedLastHurtBy;
   @Nullable
   private LivingEntity trustedLastHurt;
   private int timestamp;

   public Fox$DefendTrustedTargetGoal(
      final Class<LivingEntity> this$0,
      final boolean targetType,
      final boolean mustSee,
      @Nullable final TargetingConditions$Selector mustReach,
      final TargetingConditions$Selector subselector
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, targetType, 10, mustSee, mustReach, subselector);
   }

   @Override
   public boolean canUse() {
      if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
         return false;
      } else {
         ServerLevel level = getServerLevel(this.this$0.level());

         for (EntityReference<LivingEntity> trustedReference : this.this$0.getTrustedEntities().toList()) {
            LivingEntity trustedEntity = trustedReference.getEntity(level, LivingEntity.class);
            if (trustedEntity != null) {
               this.trustedLastHurt = trustedEntity;
               this.trustedLastHurtBy = trustedEntity.getLastHurtByMob();
               int timestamp = trustedEntity.getLastHurtByMobTimestamp();
               return timestamp != this.timestamp && this.canAttack(this.trustedLastHurtBy, this.targetConditions);
            }
         }

         return false;
      }
   }

   @Override
   public void start() {
      this.setTarget(this.trustedLastHurtBy);
      this.target = this.trustedLastHurtBy;
      if (this.trustedLastHurt != null) {
         this.timestamp = this.trustedLastHurt.getLastHurtByMobTimestamp();
      }

      this.this$0.playSound(SoundEvents.FOX_AGGRO, 1.0F, 1.0F);
      this.this$0.setDefending(true);
      this.this$0.wakeUp();
      super.start();
   }
}
