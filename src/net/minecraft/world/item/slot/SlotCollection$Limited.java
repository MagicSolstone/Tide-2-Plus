package net.minecraft.world.item.slot;

import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

public record SlotCollection$Limited(SlotCollection slots, int limit) implements SlotCollection {
   @Override
   public Stream<ItemStack> itemCopies() {
      return this.slots.itemCopies().limit(this.limit);
   }

   @Override
   public int size() {
      return Math.min(this.slots.size(), this.limit);
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      return this.slots.replaceSlotItems(items, slotSelector.limit(this.limit));
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      this.slots.modifySlots(consumer, slotSelector.limit(this.limit));
   }

   @Override
   public SlotCollection limit(final int limit) {
      return new SlotCollection$Limited(this.slots, Math.min(this.limit, limit));
   }
}
