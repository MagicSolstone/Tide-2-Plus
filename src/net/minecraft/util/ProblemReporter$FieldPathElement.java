package net.minecraft.util;

public record ProblemReporter$FieldPathElement(String name) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return "." + this.name;
   }
}
