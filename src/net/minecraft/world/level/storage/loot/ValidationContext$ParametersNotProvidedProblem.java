package net.minecraft.world.level.storage.loot;

import java.util.Set;
import net.minecraft.util.ProblemReporter$Problem;
import net.minecraft.util.context.ContextKey;

public record ValidationContext$ParametersNotProvidedProblem(Set<ContextKey<?>> notProvided) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Parameters " + this.notProvided + " are not provided in this context";
   }
}
