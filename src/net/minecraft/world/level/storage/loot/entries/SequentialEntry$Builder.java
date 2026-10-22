package net.minecraft.world.level.storage.loot.entries;

public class SequentialEntry$Builder extends CompositeEntryBase$Builder<SequentialEntry, SequentialEntry$Builder> {
   public SequentialEntry$Builder(final LootPoolEntryContainer$Builder<?>... entries) {
      super(entries);
   }

   protected SequentialEntry$Builder getThis() {
      return this;
   }

   @Override
   public SequentialEntry$Builder then(final LootPoolEntryContainer$Builder<?> other) {
      this.addEntry(other);
      return this;
   }

   @Override
   public LootPoolEntryContainer build() {
      return this.build(SequentialEntry::new);
   }
}
