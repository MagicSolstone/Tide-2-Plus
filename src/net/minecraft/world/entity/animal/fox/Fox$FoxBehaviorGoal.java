package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

abstract class Fox$FoxBehaviorGoal extends Goal {
   private final TargetingConditions alertableTargeting;

   private Fox$FoxBehaviorGoal(final Fox this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.alertableTargeting = TargetingConditions.forCombat().range(12.0).ignoreLineOfSight().selector(new Fox$FoxAlertableEntitiesSelector(this.this$0));
   }

   protected boolean hasShelter() {
      BlockPos foxPos = BlockPos.containing(this.this$0.getX(), this.this$0.getBoundingBox().maxY, this.this$0.getZ());
      return !this.this$0.level().canSeeSky(foxPos) && this.this$0.getWalkTargetValue(foxPos) >= 0.0F;
   }

   protected boolean alertable() {
      return !getServerLevel(this.this$0.level())
         .getNearbyEntities(LivingEntity.class, this.alertableTargeting, this.this$0, this.this$0.getBoundingBox().inflate(12.0, 6.0, 12.0))
         .isEmpty();
   }
}
