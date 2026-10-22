package net.minecraft.world.entity.animal.rabbit;

import net.minecraft.world.entity.ai.goal.PanicGoal;

class Rabbit$RabbitPanicGoal extends PanicGoal {
   private final Rabbit rabbit;

   public Rabbit$RabbitPanicGoal(final Rabbit rabbit, final double speedModifier) {
      super(rabbit, speedModifier);
      this.rabbit = rabbit;
   }

   @Override
   public void tick() {
      super.tick();
      this.rabbit.setSpeedModifier(this.speedModifier);
   }
}
