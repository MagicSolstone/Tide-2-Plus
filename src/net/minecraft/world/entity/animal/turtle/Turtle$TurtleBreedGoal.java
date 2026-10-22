package net.minecraft.world.entity.animal.turtle;

import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.level.gamerules.GameRules;

class Turtle$TurtleBreedGoal extends BreedGoal {
   private final Turtle turtle;

   public Turtle$TurtleBreedGoal(final Turtle turtle, final double speedModifier) {
      super(turtle, speedModifier);
      this.turtle = turtle;
   }

   @Override
   public boolean canUse() {
      return super.canUse() && !this.turtle.hasEgg();
   }

   @Override
   protected void breed() {
      ServerPlayer loveCause = this.animal.getLoveCause();
      if (loveCause == null && this.partner.getLoveCause() != null) {
         loveCause = this.partner.getLoveCause();
      }

      if (loveCause != null) {
         loveCause.awardStat(Stats.ANIMALS_BRED);
         CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, null);
      }

      this.turtle.setHasEgg(true);
      this.animal.setAge(6000);
      this.partner.setAge(6000);
      this.animal.resetLove();
      this.partner.resetLove();
      RandomSource random = this.animal.getRandom();
      if (getServerLevel(this.level).getGameRules().get(GameRules.MOB_DROPS)) {
         this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
      }
   }
}
