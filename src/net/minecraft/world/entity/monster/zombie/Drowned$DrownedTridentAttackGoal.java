package net.minecraft.world.entity.monster.zombie;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Items;

class Drowned$DrownedTridentAttackGoal extends RangedAttackGoal {
   private final Drowned drowned;

   public Drowned$DrownedTridentAttackGoal(final RangedAttackMob mob, final double speedModifier, final int attackInterval, final float attackRadius) {
      super(mob, speedModifier, attackInterval, attackRadius);
      this.drowned = (Drowned)mob;
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT);
   }

   @Override
   public void start() {
      super.start();
      this.drowned.setAggressive(true);
      this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
   }

   @Override
   public void stop() {
      super.stop();
      this.drowned.stopUsingItem();
      this.drowned.setAggressive(false);
   }
}
