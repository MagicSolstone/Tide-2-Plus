package net.minecraft.world.level.storage.loot.functions;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

final class LootItemConditionalFunction$DummyBuilder extends LootItemConditionalFunction$Builder<LootItemConditionalFunction$DummyBuilder> {
   private final Function<Optional<Holder<LootItemCondition>>, LootItemFunction> constructor;

   public LootItemConditionalFunction$DummyBuilder(final Function<Optional<Holder<LootItemCondition>>, LootItemFunction> constructor) {
      this.constructor = constructor;
   }

   protected LootItemConditionalFunction$DummyBuilder getThis() {
      return this;
   }

   @Override
   public LootItemFunction build() {
      return this.constructor.apply(this.getCondition());
   }
}
