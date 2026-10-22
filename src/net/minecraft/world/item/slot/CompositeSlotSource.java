package net.minecraft.world.item.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;

public abstract class CompositeSlotSource implements SlotSource {
   protected final HolderSet<SlotSource> terms;
   private final Function<LootContext, SlotCollection> compositeSlotSource;

   protected CompositeSlotSource(final HolderSet<SlotSource> terms) {
      this.terms = terms;
      this.compositeSlotSource = group(terms);
   }

   private static Function<LootContext, SlotCollection> group(final HolderSet<SlotSource> terms) {
      if (!terms.isBound()) {
         return context -> {
            List<SlotCollection> collections = new ArrayList<>();

            for (Holder<SlotSource> term : terms) {
               collections.add(term.value().provide(context));
            }

            return SlotCollection.concat(collections);
         };
      } else {
         return switch (terms.size()) {
            case 0 -> var0 -> SlotCollection.EMPTY;
            case 1 -> {
               Holder<SlotSource> term = terms.get(0);
               yield context -> term.value().provide(context);
            }
            case 2 -> {
               Holder<SlotSource> first = terms.get(0);
               Holder<SlotSource> second = terms.get(1);
               yield context -> SlotCollection.concat(first.value().provide(context), second.value().provide(context));
            }
            default -> context -> {
               List<SlotCollection> collections = new ArrayList<>();

               for (Holder<SlotSource> term : terms) {
                  collections.add(term.value().provide(context));
               }

               return SlotCollection.concat(collections);
            };
         };
      }
   }

   protected static <T extends CompositeSlotSource> MapCodec<T> createCodec(final Function<HolderSet<SlotSource>, T> factory) {
      return RecordCodecBuilder.mapCodec(i -> i.group(SlotSources.LIST_CODEC.fieldOf("terms").forGetter(t -> t.terms)).apply(i, factory));
   }

   protected static <T extends CompositeSlotSource> Codec<T> createInlineCodec(final Function<HolderSet<SlotSource>, T> factory) {
      return SlotSources.LIST_CODEC.xmap(factory, t -> t.terms);
   }

   @Override
   public abstract MapCodec<? extends CompositeSlotSource> codec();

   @Override
   public SlotCollection provide(final LootContext context) {
      return this.compositeSlotSource.apply(context);
   }

   @Override
   public void validate(final ValidationContext context) {
      SlotSource.super.validate(context);
      Validatable.validateHolderSet(context, "terms", this.terms);
   }
}
