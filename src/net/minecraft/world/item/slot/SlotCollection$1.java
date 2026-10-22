package net.minecraft.world.item.slot;

import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

class SlotCollection$1 implements SlotCollection {
   @Override
   public Stream<ItemStack> itemCopies() {
      return Stream.empty();
   }

   @Override
   public int size() {
      return 0;
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      return 0;
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
   }
}
