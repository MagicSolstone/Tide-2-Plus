package net.minecraft.util;

public record ProblemReporter$MapEntryPathElement(String name, String key) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return "." + this.name + "[" + this.key + "]";
   }
}
