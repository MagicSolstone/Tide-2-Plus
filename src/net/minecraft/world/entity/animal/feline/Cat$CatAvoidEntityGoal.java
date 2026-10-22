package net.minecraft.world.entity.animal.feline;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

class Cat$CatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
   private final Cat cat;

   public Cat$CatAvoidEntityGoal(
      final Cat cat, final Class<T> avoidClass, final float maxDist, final double walkSpeedModifier, final double sprintSpeedModifier
   ) {
      super(cat, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
      this.cat = cat;
   }

   @Override
   public boolean canUse() {
      return !this.cat.isTame() && super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      return !this.cat.isTame() && super.canContinueToUse();
   }
}
