package net.minecraft.world.entity.animal.rabbit;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

class Rabbit$RabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
   private final Rabbit rabbit;

   public Rabbit$RabbitAvoidEntityGoal(
      final Rabbit rabbit, final Class<T> avoidClass, final float maxDist, final double walkSpeedModifier, final double sprintSpeedModifier
   ) {
      super(rabbit, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
      this.rabbit = rabbit;
   }

   @Override
   public boolean canUse() {
      return this.rabbit.getVariant() != Rabbit$Variant.EVIL && super.canUse();
   }
}
