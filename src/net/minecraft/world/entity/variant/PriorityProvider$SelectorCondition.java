package net.minecraft.world.entity.variant;

import java.util.function.Predicate;

@FunctionalInterface
public interface PriorityProvider$SelectorCondition<C> extends Predicate<C> {
   static <C> PriorityProvider$SelectorCondition<C> alwaysTrue() {
      return context -> true;
   }
}
