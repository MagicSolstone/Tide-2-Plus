package net.minecraft.util;

import net.minecraft.resources.ResourceKey;

public record ProblemReporter$RootElementPathElement(ResourceKey<?> id) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return "{" + this.id.identifier() + "@" + this.id.registry() + "}";
   }
}
