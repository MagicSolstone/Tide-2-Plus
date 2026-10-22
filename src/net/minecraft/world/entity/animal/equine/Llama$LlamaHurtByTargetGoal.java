package net.minecraft.world.entity.animal.equine;

import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

class Llama$LlamaHurtByTargetGoal extends HurtByTargetGoal {
   public Llama$LlamaHurtByTargetGoal(final Llama llama) {
      super(llama);
   }

   @Override
   public boolean canContinueToUse() {
      if (this.mob instanceof Llama llama && llama.didSpit) {
         llama.setDidSpit(false);
         return false;
      } else {
         return super.canContinueToUse();
      }
   }
}
