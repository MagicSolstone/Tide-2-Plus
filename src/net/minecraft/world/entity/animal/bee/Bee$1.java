package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;

class Bee$1 extends FlyingPathNavigation {
   Bee$1(final Bee this$0, final Mob mob, final Level level) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(mob, level);
   }

   @Override
   public boolean isStableDestination(final BlockPos pos) {
      return !this.level.getBlockState(pos.below()).isAir();
   }

   @Override
   public void tick() {
      if (!this.this$0.beePollinateGoal.isPollinating()) {
         super.tick();
      }
   }
}
