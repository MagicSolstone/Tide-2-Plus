package net.minecraft.util;

public record ProblemReporter$IndexedPathElement(int index) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return "[" + this.index + "]";
   }
}
