package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public abstract class CompositeEntryBase$Builder<T extends CompositeEntryBase, B extends CompositeEntryBase$Builder<T, B>>
   extends LootPoolEntryContainer$Builder<B> {
   private final Builder<LootPoolEntryContainer> entries = ImmutableList.builder();

   public CompositeEntryBase$Builder(final LootPoolEntryContainer$Builder<?>... entries) {
      for (LootPoolEntryContainer$Builder<?> entry : entries) {
         this.entries.add(entry.build());
      }
   }

   protected Builder<LootPoolEntryContainer> addEntry(final LootPoolEntryContainer$Builder<?> entry) {
      return this.entries.add(entry.build());
   }

   protected T build(final CompositeEntryBase$CompositeEntryConstructor<T> constructor) {
      return constructor.create(this.entries.build(), this.getCondition(), this.getModifier());
   }
}
