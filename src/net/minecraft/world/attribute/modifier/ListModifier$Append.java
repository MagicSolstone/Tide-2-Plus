package net.minecraft.world.attribute.modifier;

import java.util.List;
import net.minecraft.util.Util;

public record ListModifier$Append<Element>() implements ListModifier<Element> {
   private static final ListModifier$Append<?> INSTANCE = new ListModifier$Append();

   public List<Element> apply(final List<Element> subject, final List<Element> argument) {
      if (argument.isEmpty()) {
         return subject;
      } else {
         return subject.isEmpty() ? argument : Util.join(subject, argument);
      }
   }
}
