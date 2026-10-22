package net.minecraft.world.entity.animal.fish;

import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

class Pufferfish$PufferfishPuffGoal extends Goal {
   private final Pufferfish fish;

   public Pufferfish$PufferfishPuffGoal(final Pufferfish fish) {
      this.fish = fish;
   }

   @Override
   public boolean canUse() {
      List<LivingEntity> entities = this.fish
         .level()
         .getEntitiesOfClass(
            LivingEntity.class,
            this.fish.getBoundingBox().inflate(2.0),
            target -> Pufferfish.TARGETING_CONDITIONS.test(getServerLevel(this.fish), this.fish, target)
         );
      return !entities.isEmpty();
   }

   @Override
   public void start() {
      this.fish.inflateCounter = 1;
      this.fish.deflateTimer = 0;
   }

   @Override
   public void stop() {
      this.fish.inflateCounter = 0;
   }
}
