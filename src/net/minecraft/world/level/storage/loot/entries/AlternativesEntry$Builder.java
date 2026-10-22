package net.minecraft.world.level.storage.loot.entries;

public class AlternativesEntry$Builder extends CompositeEntryBase$Builder<AlternativesEntry, AlternativesEntry$Builder> {
   public AlternativesEntry$Builder(final LootPoolEntryContainer$Builder<?>... entries) {
      super(entries);
   }

   protected AlternativesEntry$Builder getThis() {
      return this;
   }

   @Override
   public AlternativesEntry$Builder otherwise(final LootPoolEntryContainer$Builder<?> other) {
      this.addEntry(other);
      return this;
   }

   @Override
   public LootPoolEntryContainer build() {
      return this.build(AlternativesEntry::new);
   }
}
