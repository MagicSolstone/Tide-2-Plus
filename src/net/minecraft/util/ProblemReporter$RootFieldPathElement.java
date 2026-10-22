package net.minecraft.util;

public record ProblemReporter$RootFieldPathElement(String name) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return this.name;
   }
}
