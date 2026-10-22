package net.minecraft.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Util;
import net.minecraft.world.level.storage.loot.LootContext;

public class AllOfCondition extends CompositeLootItemCondition {
   public static final MapCodec<AllOfCondition> MAP_CODEC = createCodec(AllOfCondition::new);

   private AllOfCondition(final HolderSet<LootItemCondition> terms) {
      super(terms, combine(terms));
   }

   private static Predicate<LootContext> combine(final HolderSet<LootItemCondition> terms) {
      return !terms.isBound() ? context -> {
         for (Holder<LootItemCondition> entry : terms) {
            if (!entry.value().test(context)) {
               return false;
            }
         }

         return true;
      } : Util.allOf(holdersToLazyPredicates(terms));
   }

   public static AllOfCondition allOf(final HolderSet<LootItemCondition> terms) {
      return new AllOfCondition(terms);
   }

   @Override
   public MapCodec<AllOfCondition> codec() {
      return MAP_CODEC;
   }

   public static AllOfCondition$Builder allOf(final LootItemCondition$Builder... terms) {
      return new AllOfCondition$Builder(terms);
   }
}
