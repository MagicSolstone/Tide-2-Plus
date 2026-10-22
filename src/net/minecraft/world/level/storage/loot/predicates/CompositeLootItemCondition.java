package net.minecraft.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public abstract class CompositeLootItemCondition implements LootItemCondition {
   protected final HolderSet<LootItemCondition> terms;
   private final Predicate<LootContext> composedPredicate;

   protected CompositeLootItemCondition(final HolderSet<LootItemCondition> terms, final Predicate<LootContext> composedPredicate) {
      this.terms = terms;
      this.composedPredicate = composedPredicate;
   }

   protected static List<Predicate<LootContext>> holdersToLazyPredicates(final HolderSet<LootItemCondition> terms) {
      return terms.stream().map(CompositeLootItemCondition::holderToLazyPredicate).toList();
   }

   private static Predicate<LootContext> holderToLazyPredicate(final Holder<LootItemCondition> h) {
      return context -> h.value().test(context);
   }

   @Override
   public abstract MapCodec<? extends CompositeLootItemCondition> codec();

   protected static <T extends CompositeLootItemCondition> MapCodec<T> createCodec(final Function<HolderSet<LootItemCondition>, T> factory) {
      return RecordCodecBuilder.mapCodec(i -> i.group(LootItemCondition.LIST_CODEC.fieldOf("terms").forGetter(condition -> condition.terms)).apply(i, factory));
   }

   public final boolean test(final LootContext context) {
      return this.composedPredicate.test(context);
   }

   @Override
   public void validate(final ValidationContext output) {
      LootItemCondition.super.validate(output);
      Validatable.validateHolderSet(output, "terms", this.terms);
   }
}
