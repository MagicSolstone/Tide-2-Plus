package net.minecraft.world.item.slot;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

public sealed interface SlotSelector permits SlotSelector$FilteringSelector, SlotSelector$TrackingSelector {
   SlotSelector ANY_SLOT = new SlotSelector$FilteringSelector(null);
   SlotSelector EMPTY_SLOTS = selecting(ItemStack::isEmpty);
   SlotSelector NON_EMPTY_SLOTS = selecting(s -> !s.isEmpty());

   boolean trySelectSlot(ItemStack itemInSlot);

   SlotSelector filter(Predicate<? super ItemStack> predicate);

   SlotSelector limit(int limit);

   static SlotSelector selecting(final Predicate<? super ItemStack> predicate) {
      return new SlotSelector$FilteringSelector(predicate);
   }

   static SlotSelector tracking(final SlotSelector slotSelector, final MutableInt selectedCount) {
      return new SlotSelector$TrackingSelector(slotSelector, Integer.MAX_VALUE, selectedCount);
   }

   static SlotSelector tracking(final MutableInt selectedCount) {
      return tracking(ANY_SLOT, selectedCount);
   }
}
