package net.minecraft.world.level.storage.loot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer$Builder;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase$Builder;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class LootPool$Builder implements FunctionUserBuilder<LootPool$Builder>, ConditionUserBuilder<LootPool$Builder> {
   private final Builder<LootPoolEntryContainer> entries = ImmutableList.builder();
   private final Builder<Holder<LootItemCondition>> conditions = ImmutableList.builder();
   private final Builder<Holder<LootItemFunction>> functions = ImmutableList.builder();
   private Holder<ContextIntProvider> rolls = ContextIntProviders.exactly(1);
   private Holder<ContextFloatProvider> bonusRolls = ContextFloatProviders.exactly(0.0F);

   public LootPool$Builder setRolls(final Holder<ContextIntProvider> rolls) {
      this.rolls = rolls;
      return this;
   }

   public LootPool$Builder unwrap() {
      return this;
   }

   public LootPool$Builder setBonusRolls(final Holder<ContextFloatProvider> bonusRolls) {
      this.bonusRolls = bonusRolls;
      return this;
   }

   public LootPool$Builder add(final LootPoolEntryContainer$Builder<?> entry) {
      this.entries.add(entry.build());
      return this;
   }

   public LootPool$Builder addAll(final List<? extends UniformContainerBase$Builder<?>> entries) {
      for (LootPoolEntryContainer$Builder<?> entry : entries) {
         this.add(entry);
      }

      return this;
   }

   public LootPool$Builder when(final Holder<LootItemCondition> condition) {
      this.conditions.add(condition);
      return this;
   }

   public LootPool$Builder apply(final Holder<LootItemFunction> function) {
      this.functions.add(function);
      return this;
   }

   public LootPool build() {
      return new LootPool(
         this.entries.build(),
         ConditionUserBuilder.buildCondition(this.conditions.build()),
         FunctionUserBuilder.buildFunction(this.functions.build()),
         this.rolls,
         this.bonusRolls
      );
   }
}
