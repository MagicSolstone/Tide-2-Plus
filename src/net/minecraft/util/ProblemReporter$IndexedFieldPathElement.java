package net.minecraft.util;

public record ProblemReporter$IndexedFieldPathElement(String name, int index) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return "." + this.name + "[" + this.index + "]";
   }
}
