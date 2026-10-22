package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.core.HolderSet;

public class AllOfCondition$Builder extends CompositeLootItemCondition$Builder {
   public AllOfCondition$Builder(final LootItemCondition$Builder... terms) {
      super(terms);
   }

   @Override
   public AllOfCondition$Builder and(final LootItemCondition$Builder term) {
      this.addTerm(term);
      return this;
   }

   @Override
   protected LootItemCondition create(final HolderSet<LootItemCondition> terms) {
      return new AllOfCondition(terms);
   }
}
