package net.minecraft.world.item.slot;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

public interface SlotCollection {
   SlotCollection EMPTY = new SlotCollection$1();

   Stream<ItemStack> itemCopies();

   int size();

   int replaceSlotItems(ItemProvider items, SlotSelector slotSelector);

   default int replaceSlotItems(final ItemProvider items) {
      return this.replaceSlotItems(items, SlotSelector.ANY_SLOT);
   }

   void modifySlots(Consumer<? super SlotAccess> consumer, SlotSelector slotSelector);

   default void modifySlots(final Consumer<? super SlotAccess> consumer) {
      this.modifySlots(consumer, SlotSelector.ANY_SLOT);
   }

   default SlotCollection filter(final Predicate<? super ItemStack> predicate) {
      return new SlotCollection$Filtered(this, predicate);
   }

   default SlotCollection flatMap(final Function<ItemStack, ? extends SlotCollection> mapper) {
      return new SlotCollection$FlatMapped(this, mapper);
   }

   default SlotCollection limit(final int limit) {
      return new SlotCollection$Limited(this, limit);
   }

   static SlotCollection of(final SlotAccess slotAccess) {
      return new SlotCollection$2(slotAccess);
   }

   static SlotCollection of(final Collection<? extends SlotAccess> slots) {
      return (SlotCollection)(switch (slots.size()) {
         case 0 -> EMPTY;
         case 1 -> of(slots.iterator().next());
         default -> new SlotCollection$3(slots);
      });
   }

   static SlotCollection concat(final SlotCollection first, final SlotCollection second) {
      return new SlotCollection$4(first, second);
   }

   static SlotCollection concat(final List<? extends SlotCollection> terms) {
      return (SlotCollection)(switch (terms.size()) {
         case 0 -> EMPTY;
         case 1 -> (SlotCollection)terms.getFirst();
         case 2 -> concat(terms.get(0), terms.get(1));
         default -> new SlotCollection$5(terms);
      });
   }
}
