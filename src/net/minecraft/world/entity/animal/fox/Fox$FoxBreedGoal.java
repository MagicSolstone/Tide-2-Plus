package net.minecraft.world.entity.animal.fox;

import java.util.Objects;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.level.gamerules.GameRules;

class Fox$FoxBreedGoal extends BreedGoal {
   public Fox$FoxBreedGoal(final Fox this$0, final double speedModifier) {
      Objects.requireNonNull(this$0);
      super(this$0, speedModifier);
   }

   @Override
   public void start() {
      ((Fox)this.animal).clearStates();
      ((Fox)this.partner).clearStates();
      super.start();
   }

   @Override
   protected void breed() {
      Fox offspring = (Fox)this.animal.getBreedOffspring(this.level, this.partner);
      if (offspring != null) {
         ServerPlayer animalLoveCause = this.animal.getLoveCause();
         ServerPlayer partnerLoveCause = this.partner.getLoveCause();
         ServerPlayer loveCause = animalLoveCause;
         if (animalLoveCause != null) {
            offspring.addTrustedEntity(animalLoveCause);
         } else {
            loveCause = partnerLoveCause;
         }

         if (partnerLoveCause != null && animalLoveCause != partnerLoveCause) {
            offspring.addTrustedEntity(partnerLoveCause);
         }

         if (loveCause != null) {
            loveCause.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, offspring);
         }

         this.animal.setAge(6000);
         this.partner.setAge(6000);
         this.animal.resetLove();
         this.partner.resetLove();
         offspring.setAge(-24000);
         offspring.snapTo(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
         this.level.addFreshEntityWithPassengers(offspring);
         this.level.broadcastEntityEvent(this.animal, (byte)18);
         if (this.level.getGameRules().get(GameRules.MOB_DROPS)) {
            this.level
               .addFreshEntity(
                  new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1)
               );
         }
      }
   }
}
