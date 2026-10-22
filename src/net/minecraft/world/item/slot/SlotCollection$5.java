package net.minecraft.world.item.slot;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

class SlotCollection$5 implements SlotCollection {
   SlotCollection$5(final List val$terms) {
      this.val$terms = val$termsx;
   }

   @Override
   public Stream<ItemStack> itemCopies() {
      return this.val$terms.stream().flatMap(SlotCollection::itemCopies);
   }

   @Override
   public int size() {
      return this.val$terms.stream().mapToInt(SlotCollection::size).sum();
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      int successCount = 0;

      for (SlotCollection slots : this.val$terms) {
         successCount += slots.replaceSlotItems(items, slotSelector);
      }

      return successCount;
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      for (SlotCollection slots : this.val$terms) {
         slots.modifySlots(consumer, slotSelector);
      }
   }
}
