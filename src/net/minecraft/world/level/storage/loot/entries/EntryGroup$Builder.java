package net.minecraft.world.level.storage.loot.entries;

public class EntryGroup$Builder extends CompositeEntryBase$Builder<EntryGroup, EntryGroup$Builder> {
   public EntryGroup$Builder(final LootPoolEntryContainer$Builder<?>... entries) {
      super(entries);
   }

   protected EntryGroup$Builder getThis() {
      return this;
   }

   @Override
   public EntryGroup$Builder append(final LootPoolEntryContainer$Builder<?> other) {
      this.addEntry(other);
      return this;
   }

   @Override
   public LootPoolEntryContainer build() {
      return this.build(EntryGroup::new);
   }
}
