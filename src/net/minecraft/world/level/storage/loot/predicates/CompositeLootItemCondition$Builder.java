package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

public abstract class CompositeLootItemCondition$Builder implements LootItemCondition$Builder {
   private final Builder<Holder<LootItemCondition>> terms = ImmutableList.builder();

   protected CompositeLootItemCondition$Builder(final LootItemCondition$Builder... terms) {
      for (LootItemCondition$Builder term : terms) {
         this.terms.add(Holder.direct(term.build()));
      }
   }

   public void addTerm(final LootItemCondition$Builder term) {
      this.addTerm(Holder.direct(term.build()));
   }

   public void addTerm(final Holder<LootItemCondition> term) {
      this.terms.add(term);
   }

   @Override
   public LootItemCondition build() {
      return this.create(HolderSet.direct(this.terms.build()));
   }

   protected abstract LootItemCondition create(HolderSet<LootItemCondition> terms);
}
