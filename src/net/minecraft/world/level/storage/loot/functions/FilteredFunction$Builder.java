package net.minecraft.world.level.storage.loot.functions;

import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.Holder;

public class FilteredFunction$Builder extends LootItemConditionalFunction$Builder<FilteredFunction$Builder> {
   private final ItemPredicate itemPredicate;
   private Optional<Holder<LootItemFunction>> onPass = Optional.empty();
   private Optional<Holder<LootItemFunction>> onFail = Optional.empty();

   private FilteredFunction$Builder(final ItemPredicate itemPredicate) {
      this.itemPredicate = itemPredicate;
   }

   protected FilteredFunction$Builder getThis() {
      return this;
   }

   public FilteredFunction$Builder onPass(final LootItemFunction onPass) {
      this.onPass = Optional.of(Holder.direct(onPass));
      return this;
   }

   public FilteredFunction$Builder onFail(final LootItemFunction onFail) {
      this.onFail = Optional.of(Holder.direct(onFail));
      return this;
   }

   @Override
   public LootItemFunction build() {
      return new FilteredFunction(this.getCondition(), this.itemPredicate, this.onPass, this.onFail);
   }
}
