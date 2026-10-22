package net.minecraft.world.level.storage.loot;

import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter$RootElementPathElement;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.slot.SlotSource;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public record LootDataType<T extends Validatable>(ResourceKey<Registry<T>> registryKey, LootDataType$ContextGetter<T> contextGetter) {
   public static final LootDataType<LootItemCondition> PREDICATE = new LootDataType<>(
      Registries.PREDICATE, LootDataType$ContextGetter.constant(LootContextParamSets.ALL_PARAMS)
   );
   public static final LootDataType<LootItemFunction> MODIFIER = new LootDataType<>(
      Registries.ITEM_MODIFIER, LootDataType$ContextGetter.constant(LootContextParamSets.ALL_PARAMS)
   );
   public static final LootDataType<SlotSource> SLOT_SOURCE = new LootDataType<>(
      Registries.SLOT_SOURCE, LootDataType$ContextGetter.constant(LootContextParamSets.ALL_PARAMS)
   );
   public static final LootDataType<LootTable> TABLE = new LootDataType<>(Registries.LOOT_TABLE, LootTable::getParamSet);
   public static final LootDataType<ContextFloatProvider> FLOAT_PROVIDER = new LootDataType<>(
      Registries.CONTEXT_FLOAT_PROVIDER, LootDataType$ContextGetter.constant(LootContextParamSets.ALL_PARAMS)
   );
   public static final LootDataType<ContextIntProvider> INT_PROVIDER = new LootDataType<>(
      Registries.CONTEXT_INT_PROVIDER, LootDataType$ContextGetter.constant(LootContextParamSets.ALL_PARAMS)
   );

   public void runValidation(final ValidationContextSource contextSource, final ResourceKey<T> key, final T value) {
      ContextKeySet contextKeys = this.contextGetter.context(value);
      ValidationContext rootContext = contextSource.context(contextKeys).enterElement(new ProblemReporter$RootElementPathElement(key), key);
      value.validate(rootContext);
   }

   public void runValidation(final ValidationContextSource contextSource, final HolderLookup<T> lookup) {
      lookup.listElements().forEach(holder -> this.runValidation(contextSource, holder.key(), holder.value()));
   }

   public void runValidation(final ValidationContextSource contextSource, final HolderLookup$Provider registries) {
      HolderLookup<T> registry = registries.lookupOrThrow(this.registryKey());
      this.runValidation(contextSource, registry);
   }

   public void runValidationIfPresent(final ValidationContextSource contextSource, final HolderLookup$Provider registries) {
      registries.lookup(this.registryKey()).ifPresent(registry -> this.runValidation(contextSource, registry));
   }

   public static Stream<LootDataType<?>> values() {
      return Stream.of(PREDICATE, MODIFIER, SLOT_SOURCE, TABLE, FLOAT_PROVIDER, INT_PROVIDER);
   }
}
