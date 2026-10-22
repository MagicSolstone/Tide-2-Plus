package net.minecraft.world.entity.animal.polarbear;

import java.util.Objects;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

class PolarBear$PolarBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
   public PolarBear$PolarBearAttackPlayersGoal(final PolarBear this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x, Player.class, 20, true, true, null);
   }

   @Override
   public boolean canUse() {
      if (this.this$0.isBaby()) {
         return false;
      } else {
         if (super.canUse()) {
            for (PolarBear bear : this.this$0.level().getEntitiesOfClass(PolarBear.class, this.this$0.getBoundingBox().inflate(8.0, 4.0, 8.0))) {
               if (bear.isBaby()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   @Override
   protected double getFollowDistance() {
      return super.getFollowDistance() * 0.5;
   }
}
