package net.minecraft.world.level.storage;

import net.minecraft.nbt.TagType;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueInput$UnexpectedNonNumberProblem(String name, TagType<?> actual) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Expected field '" + this.name + "' to contain number, but got " + this.actual.getName();
   }
}
