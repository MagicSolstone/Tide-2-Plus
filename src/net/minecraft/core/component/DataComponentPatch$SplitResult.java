package net.minecraft.core.component;

import java.util.Set;

public record DataComponentPatch$SplitResult(DataComponentMap added, Set<DataComponentType<?>> removed) {
   public static final DataComponentPatch$SplitResult EMPTY = new DataComponentPatch$SplitResult(DataComponentMap.EMPTY, Set.of());
}
