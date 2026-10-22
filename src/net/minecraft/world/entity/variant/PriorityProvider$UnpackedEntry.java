package net.minecraft.world.entity.variant;

import java.util.Comparator;

public record PriorityProvider$UnpackedEntry<C, T>(T entry, int priority, PriorityProvider$SelectorCondition<C> condition) {
   public static final Comparator<PriorityProvider$UnpackedEntry<?, ?>> HIGHEST_PRIORITY_FIRST = Comparator.comparingInt(
         PriorityProvider$UnpackedEntry::priority
      )
      .reversed();
}
