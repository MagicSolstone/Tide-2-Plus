package net.minecraft.world.entity.animal.armadillo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;

public class ArmadilloAi$ArmadilloPanic extends AnimalPanic<Armadillo> {
   public ArmadilloAi$ArmadilloPanic(final float speedMultiplier) {
      super(speedMultiplier, mob -> DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
   }

   protected void start(final ServerLevel level, final Armadillo armadillo, final long timestamp) {
      armadillo.rollOut();
      super.start(level, armadillo, timestamp);
   }
}
