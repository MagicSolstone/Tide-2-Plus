package net.minecraft.world.item.slot;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

class SlotCollection$3 implements SlotCollection {
   SlotCollection$3(final Collection val$slots) {
      this.val$slots = val$slotsx;
   }

   @Override
   public Stream<ItemStack> itemCopies() {
      return this.val$slots.stream().map(SlotAccess::get).map(ItemStack::copy);
   }

   @Override
   public int size() {
      return this.val$slots.size();
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      Iterator<? extends SlotAccess> iterator = this.val$slots.iterator();
      int successCount = 0;

      while (iterator.hasNext() && items.hasNext()) {
         SlotAccess slot = iterator.next();
         if (slotSelector.trySelectSlot(slot.get()) && slot.set(items.next())) {
            successCount++;
         }
      }

      return successCount;
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      for (SlotAccess slot : this.val$slots) {
         if (slotSelector.trySelectSlot(slot.get())) {
            consumer.accept(slot);
         }
      }
   }
}
