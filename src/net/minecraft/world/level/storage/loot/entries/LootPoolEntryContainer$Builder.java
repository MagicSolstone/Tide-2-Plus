package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class LootPoolEntryContainer$Builder<T extends LootPoolEntryContainer$Builder<T>> implements ConditionUserBuilder<T>, FunctionUserBuilder<T> {
   private final Builder<Holder<LootItemCondition>> conditions = ImmutableList.builder();
   private final Builder<Holder<LootItemFunction>> modifiers = ImmutableList.builder();

   protected abstract T getThis();

   public T when(final Holder<LootItemCondition> condition) {
      this.conditions.add(condition);
      return this.getThis();
   }

   public T apply(final Holder<LootItemFunction> function) {
      this.modifiers.add(function);
      return this.getThis();
   }

   public final T unwrap() {
      return this.getThis();
   }

   protected Optional<Holder<LootItemCondition>> getCondition() {
      return ConditionUserBuilder.buildCondition(this.conditions.build());
   }

   protected Optional<Holder<LootItemFunction>> getModifier() {
      return FunctionUserBuilder.buildFunction(this.modifiers.build());
   }

   public AlternativesEntry$Builder otherwise(final LootPoolEntryContainer$Builder<?> other) {
      return new AlternativesEntry$Builder(this, other);
   }

   public EntryGroup$Builder append(final LootPoolEntryContainer$Builder<?> other) {
      return new EntryGroup$Builder(this, other);
   }

   public SequentialEntry$Builder then(final LootPoolEntryContainer$Builder<?> other) {
      return new SequentialEntry$Builder(this, other);
   }

   public abstract LootPoolEntryContainer build();
}
