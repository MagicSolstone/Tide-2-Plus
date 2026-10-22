package net.minecraft.world.entity.animal.equine;

import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.wolf.Wolf;

class Llama$LlamaAttackWolfGoal extends NearestAttackableTargetGoal<Wolf> {
   public Llama$LlamaAttackWolfGoal(final Llama llama) {
      super(llama, Wolf.class, 16, false, true, (target, level) -> !((Wolf)target).isTame());
   }

   @Override
   protected double getFollowDistance() {
      return super.getFollowDistance() * 0.25;
   }
}
