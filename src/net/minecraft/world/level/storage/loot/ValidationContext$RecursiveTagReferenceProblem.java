package net.minecraft.world.level.storage.loot;

import net.minecraft.tags.TagKey;
import net.minecraft.util.ProblemReporter$Problem;

public record ValidationContext$RecursiveTagReferenceProblem(TagKey<?> referenced) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "#" + this.referenced.location() + " of type " + this.referenced.registry() + " is recursively called";
   }

   @Override
   public boolean isFatal() {
      return true;
   }
}
