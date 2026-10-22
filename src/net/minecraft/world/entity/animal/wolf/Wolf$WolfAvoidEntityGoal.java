package net.minecraft.world.entity.animal.wolf;

import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.equine.Llama;

class Wolf$WolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
   private final Wolf wolf;

   public Wolf$WolfAvoidEntityGoal(
      final Wolf this$0, final Class<T> wolf, final float avoidClass, final double maxDist, final double walkSpeedModifier, final double sprintSpeedModifier
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(wolf, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
      this.wolf = wolf;
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.toAvoid instanceof Llama ? !this.wolf.isTame() && this.avoidLlama((Llama)this.toAvoid) : false;
   }

   private boolean avoidLlama(final Llama llama) {
      return llama.getStrength() >= Wolf.access$000(this.this$0).nextInt(5);
   }

   @Override
   public void start() {
      this.this$0.setTarget(null);
      super.start();
   }

   @Override
   public void tick() {
      this.this$0.setTarget(null);
      super.tick();
   }
}
