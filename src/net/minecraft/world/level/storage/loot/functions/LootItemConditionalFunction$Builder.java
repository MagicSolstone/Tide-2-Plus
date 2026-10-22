package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class LootItemConditionalFunction$Builder<T extends LootItemConditionalFunction$Builder<T>>
   implements LootItemFunction$Builder,
   ConditionUserBuilder<T> {
   private final Builder<Holder<LootItemCondition>> conditions = ImmutableList.builder();

   public T when(final Holder<LootItemCondition> condition) {
      this.conditions.add(condition);
      return this.getThis();
   }

   public final T unwrap() {
      return this.getThis();
   }

   protected abstract T getThis();

   protected Optional<Holder<LootItemCondition>> getCondition() {
      return ConditionUserBuilder.buildCondition(this.conditions.build());
   }
}
