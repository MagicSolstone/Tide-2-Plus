package net.minecraft.world.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.SwingAnimation;
import org.jspecify.annotations.Nullable;

public class LivingEntity$SwingState {
   @Nullable
   private LivingEntity$SwingDescription currentSwing;
   private int ticks;
   private float oldAnimation;
   private float animation;

   public boolean startIfAble(final InteractionHand hand, final SwingAnimation animation, final int durationTicks) {
      if (this.currentSwing != null && this.ticks <= this.currentSwing.durationTicks / 2 && this.ticks > 0) {
         return false;
      } else {
         this.start(hand, animation, durationTicks);
         return true;
      }
   }

   private void start(final InteractionHand hand, final SwingAnimation animation, final int durationTicks) {
      LivingEntity$SwingDescription oldSwing = this.currentSwing;
      this.currentSwing = new LivingEntity$SwingDescription(hand, animation, durationTicks);
      if (!this.currentSwing.equals(oldSwing)) {
         this.oldAnimation = 0.0F;
         this.animation = 0.0F;
      }

      this.ticks = 0;
   }

   public void tick() {
      this.oldAnimation = this.animation;
      if (this.currentSwing != null) {
         if (this.currentSwing.durationTicks > 0) {
            this.animation = Math.min((float)this.ticks / this.currentSwing.durationTicks, 1.0F);
         }

         if (this.ticks++ > this.currentSwing.durationTicks) {
            this.currentSwing = null;
            this.animation = 0.0F;
         }
      } else {
         this.animation = 0.0F;
      }
   }

   public float getAnimation(final float partialTicks) {
      return this.animation >= this.oldAnimation
         ? Mth.lerp(partialTicks, this.oldAnimation, this.animation)
         : Mth.lerp(partialTicks, this.oldAnimation, this.animation + 1.0F);
   }

   public boolean isSwinging() {
      return this.currentSwing != null;
   }
}
