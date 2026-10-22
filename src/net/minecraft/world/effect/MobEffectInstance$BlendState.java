package net.minecraft.world.effect;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

class MobEffectInstance$BlendState {
   private float factor;
   private float factorPreviousFrame;

   private MobEffectInstance$BlendState() {
   }

   public void setImmediate(final MobEffectInstance instance) {
      this.factor = hasEffect(instance) ? 1.0F : 0.0F;
      this.factorPreviousFrame = this.factor;
   }

   public void copyFrom(final MobEffectInstance$BlendState other) {
      this.factor = other.factor;
      this.factorPreviousFrame = other.factorPreviousFrame;
   }

   public void tick(final MobEffectInstance instance) {
      this.factorPreviousFrame = this.factor;
      boolean hasEffect = hasEffect(instance);
      float target = hasEffect ? 1.0F : 0.0F;
      if (this.factor != target) {
         MobEffect effect = instance.getEffect().value();
         int blendDuration = hasEffect ? effect.getBlendInDurationTicks() : effect.getBlendOutDurationTicks();
         if (blendDuration == 0) {
            this.factor = target;
         } else {
            float maxDeltaPerTick = 1.0F / blendDuration;
            this.factor = this.factor + Mth.clamp(target - this.factor, -maxDeltaPerTick, maxDeltaPerTick);
         }
      }
   }

   private static boolean hasEffect(final MobEffectInstance instance) {
      return !instance.endsWithin(instance.getEffect().value().getBlendOutAdvanceTicks());
   }

   public float getFactor(final LivingEntity livingEntity, final float partialTickTime) {
      if (livingEntity.isRemoved()) {
         this.factorPreviousFrame = this.factor;
      }

      return Mth.lerp(partialTickTime, this.factorPreviousFrame, this.factor);
   }
}
