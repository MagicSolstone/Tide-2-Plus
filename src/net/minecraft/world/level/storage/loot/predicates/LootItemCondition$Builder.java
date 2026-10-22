package net.minecraft.world.level.storage.loot.predicates;

@FunctionalInterface
public interface LootItemCondition$Builder {
   LootItemCondition build();

   default LootItemCondition$Builder invert() {
      return InvertedLootItemCondition.invert(this);
   }

   default AnyOfCondition$Builder or(final LootItemCondition$Builder other) {
      return AnyOfCondition.anyOf(this, other);
   }

   default AllOfCondition$Builder and(final LootItemCondition$Builder other) {
      return AllOfCondition.allOf(this, other);
   }
}
