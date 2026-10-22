package net.minecraft.world.level.storage.loot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class LootTable$Builder implements FunctionUserBuilder<LootTable$Builder> {
   private final Builder<LootPool> pools = ImmutableList.builder();
   private final Builder<Holder<LootItemFunction>> functions = ImmutableList.builder();
   private ContextKeySet paramSet = LootTable.DEFAULT_PARAM_SET;
   private Optional<Identifier> randomSequence = Optional.empty();

   public LootTable$Builder withPool(final LootPool$Builder pool) {
      this.pools.add(pool.build());
      return this;
   }

   public LootTable$Builder setParamSet(final ContextKeySet paramSet) {
      this.paramSet = paramSet;
      return this;
   }

   public LootTable$Builder setRandomSequence(final Identifier key) {
      this.randomSequence = Optional.of(key);
      return this;
   }

   public LootTable$Builder apply(final Holder<LootItemFunction> function) {
      this.functions.add(function);
      return this;
   }

   public LootTable$Builder unwrap() {
      return this;
   }

   public LootTable build() {
      return new LootTable(this.paramSet, this.randomSequence, this.pools.build(), FunctionUserBuilder.buildFunction(this.functions.build()));
   }
}
