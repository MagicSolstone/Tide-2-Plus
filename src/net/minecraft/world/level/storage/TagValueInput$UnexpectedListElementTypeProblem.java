package net.minecraft.world.level.storage;

import net.minecraft.nbt.TagType;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueInput$UnexpectedListElementTypeProblem(String name, int index, TagType<?> expected, TagType<?> actual) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Expected list '"
         + this.name
         + "' to contain at index "
         + this.index
         + " value of type "
         + this.expected.getName()
         + ", but got "
         + this.actual.getName();
   }
}
