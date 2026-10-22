package net.minecraft.world.entity.animal.panda;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

class Panda$PandaAvoidGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
   private final Panda panda;

   public Panda$PandaAvoidGoal(
      final Panda panda, final Class<T> avoidClass, final float maxDist, final double walkSpeedModifier, final double sprintSpeedModifier
   ) {
      super(panda, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_SPECTATORS);
      this.panda = panda;
   }

   @Override
   public boolean canUse() {
      return this.panda.isWorried() && this.panda.canPerformAction() && super.canUse();
   }
}
