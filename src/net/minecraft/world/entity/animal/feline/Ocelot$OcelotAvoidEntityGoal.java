package net.minecraft.world.entity.animal.feline;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

class Ocelot$OcelotAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
   private final Ocelot ocelot;

   public Ocelot$OcelotAvoidEntityGoal(
      final Ocelot ocelot, final Class<T> avoidClass, final float maxDist, final double walkSpeedModifier, final double sprintSpeedModifier
   ) {
      super(ocelot, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
      this.ocelot = ocelot;
   }

   @Override
   public boolean canUse() {
      return !this.ocelot.isTrusting() && super.canUse();
   }

   @Override
   public boolean canContinueToUse() {
      return !this.ocelot.isTrusting() && super.canContinueToUse();
   }
}
