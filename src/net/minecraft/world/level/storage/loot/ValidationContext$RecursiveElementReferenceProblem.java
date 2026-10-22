package net.minecraft.world.level.storage.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter$Problem;

public record ValidationContext$RecursiveElementReferenceProblem(ResourceKey<?> referenced) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return this.referenced.identifier() + " of type " + this.referenced.registry() + " is recursively called";
   }

   @Override
   public boolean isFatal() {
      return true;
   }
}
