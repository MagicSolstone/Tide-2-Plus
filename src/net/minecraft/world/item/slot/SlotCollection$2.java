package net.minecraft.world.item.slot;

import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

class SlotCollection$2 implements SlotCollection {
   SlotCollection$2(final SlotAccess val$slotAccess) {
      this.val$slotAccess = val$slotAccessx;
   }

   @Override
   public Stream<ItemStack> itemCopies() {
      return Stream.of(this.val$slotAccess.get().copy());
   }

   @Override
   public int size() {
      return 1;
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      if (items.hasNext() && slotSelector.trySelectSlot(this.val$slotAccess.get())) {
         return this.val$slotAccess.set(items.next()) ? 1 : 0;
      } else {
         return 0;
      }
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      if (slotSelector.trySelectSlot(this.val$slotAccess.get())) {
         consumer.accept(this.val$slotAccess);
      }
   }
}
