package net.minecraft.util;

import java.util.function.Consumer;

public interface DependencySorter$Entry<K> {
   void visitRequiredDependencies(final Consumer<K> output);

   void visitOptionalDependencies(final Consumer<K> output);
}
