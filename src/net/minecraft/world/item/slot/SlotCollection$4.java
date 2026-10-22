package net.minecraft.world.item.slot;

import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

class SlotCollection$4 implements SlotCollection {
   SlotCollection$4(final SlotCollection val$first, final SlotCollection val$second) {
      this.val$first = val$firstx;
      this.val$second = val$secondx;
   }

   @Override
   public Stream<ItemStack> itemCopies() {
      return Stream.concat(this.val$first.itemCopies(), this.val$second.itemCopies());
   }

   @Override
   public int size() {
      return this.val$first.size() + this.val$second.size();
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      int firstCount = this.val$first.replaceSlotItems(items, slotSelector);
      int secondCount = this.val$second.replaceSlotItems(items, slotSelector);
      return firstCount + secondCount;
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      this.val$first.modifySlots(consumer, slotSelector);
      this.val$second.modifySlots(consumer, slotSelector);
   }
}
