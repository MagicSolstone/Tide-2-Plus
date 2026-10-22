package net.minecraft.world.level.storage.loot;

import net.minecraft.util.ProblemReporter$Problem;

public record ValidationContext$MinBoundsProblem(int minBounds) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "List must contain at least " + this.minBounds + " element(s)";
   }
}
