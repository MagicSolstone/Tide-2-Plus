package net.minecraft.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Util;
import net.minecraft.world.level.storage.loot.LootContext;

public class AnyOfCondition extends CompositeLootItemCondition {
   public static final MapCodec<AnyOfCondition> MAP_CODEC = createCodec(AnyOfCondition::new);

   private AnyOfCondition(final HolderSet<LootItemCondition> terms) {
      super(terms, combine(terms));
   }

   private static Predicate<LootContext> combine(final HolderSet<LootItemCondition> terms) {
      return !terms.isBound() ? context -> {
         for (Holder<LootItemCondition> entry : terms) {
            if (entry.value().test(context)) {
               return true;
            }
         }

         return false;
      } : Util.anyOf(holdersToLazyPredicates(terms));
   }

   @Override
   public MapCodec<AnyOfCondition> codec() {
      return MAP_CODEC;
   }

   public static AnyOfCondition$Builder anyOf(final LootItemCondition$Builder... terms) {
      return new AnyOfCondition$Builder(terms);
   }
}
