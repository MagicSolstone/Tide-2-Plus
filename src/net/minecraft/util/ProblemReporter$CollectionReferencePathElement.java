package net.minecraft.util;

import net.minecraft.tags.TagKey;

public record ProblemReporter$CollectionReferencePathElement(TagKey<?> id) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return "->{#" + this.id.location() + "@" + this.id.registry() + "}";
   }
}
