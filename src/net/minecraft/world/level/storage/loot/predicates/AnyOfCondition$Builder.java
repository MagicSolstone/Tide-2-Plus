package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

public class AnyOfCondition$Builder extends CompositeLootItemCondition$Builder {
   public AnyOfCondition$Builder(final LootItemCondition$Builder... terms) {
      super(terms);
   }

   @Override
   public AnyOfCondition$Builder or(final LootItemCondition$Builder term) {
      this.addTerm(term);
      return this;
   }

   public AnyOfCondition$Builder or(final Holder<LootItemCondition> term) {
      this.addTerm(term);
      return this;
   }

   @Override
   protected LootItemCondition create(final HolderSet<LootItemCondition> terms) {
      return new AnyOfCondition(terms);
   }
}
