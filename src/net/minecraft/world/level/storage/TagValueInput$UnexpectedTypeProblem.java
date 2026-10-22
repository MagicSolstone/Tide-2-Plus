package net.minecraft.world.level.storage;

import net.minecraft.nbt.TagType;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueInput$UnexpectedTypeProblem(String name, TagType<?> expected, TagType<?> actual) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Expected field '" + this.name + "' to contain value of type " + this.expected.getName() + ", but got " + this.actual.getName();
   }
}
