package net.minecraft.tags;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.DependencySorter$Entry;

record TagLoader$SortingEntry(List<TagLoader$EntryWithSource> entries) implements DependencySorter$Entry<Identifier> {
   @Override
   public void visitRequiredDependencies(final Consumer<Identifier> output) {
      this.entries.forEach(e -> e.entry.visitRequiredDependencies(output));
   }

   @Override
   public void visitOptionalDependencies(final Consumer<Identifier> output) {
      this.entries.forEach(e -> e.entry.visitOptionalDependencies(output));
   }
}
