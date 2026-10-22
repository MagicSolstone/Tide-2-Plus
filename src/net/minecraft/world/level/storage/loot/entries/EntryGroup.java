package net.minecraft.world.level.storage.loot.entries;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EntryGroup extends CompositeEntryBase {
   public static final MapCodec<EntryGroup> MAP_CODEC = createCodec(EntryGroup::new);

   public EntryGroup(
      final List<LootPoolEntryContainer> children, final Optional<Holder<LootItemCondition>> condition, final Optional<Holder<LootItemFunction>> modifier
   ) {
      super(children, condition, modifier);
   }

   @Override
   public MapCodec<EntryGroup> codec() {
      return MAP_CODEC;
   }

   @Override
   protected ComposableEntryContainer compose(final List<? extends ComposableEntryContainer> entries) {
      return switch (entries.size()) {
         case 0 -> ALWAYS_TRUE;
         case 1 -> (ComposableEntryContainer)entries.get(0);
         case 2 -> {
            ComposableEntryContainer first = entries.get(0);
            ComposableEntryContainer second = entries.get(1);
            yield (context, output) -> {
               first.expand(context, output);
               second.expand(context, output);
               return true;
            };
         }
         default -> (context, output) -> {
            for (ComposableEntryContainer entry : entries) {
               entry.expand(context, output);
            }

            return true;
         };
      };
   }

   public static EntryGroup$Builder list(final LootPoolEntryContainer$Builder<?>... entries) {
      return new EntryGroup$Builder(entries);
   }
}
