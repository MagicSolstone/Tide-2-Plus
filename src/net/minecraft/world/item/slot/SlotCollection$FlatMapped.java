package net.minecraft.world.item.slot;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;

public record SlotCollection$FlatMapped(SlotCollection slots, Function<ItemStack, ? extends SlotCollection> mapper) implements SlotCollection {
   @Override
   public Stream<ItemStack> itemCopies() {
      return this.slots.itemCopies().map(this.mapper).flatMap(SlotCollection::itemCopies);
   }

   @Override
   public int size() {
      return this.slots.itemCopies().map(this.mapper).mapToInt(SlotCollection::size).sum();
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      MutableInt successCount = new MutableInt();
      this.slots.modifySlots(slot -> {
         SlotCollection mapped = this.mapper.apply(slot.get());
         successCount.add(mapped.replaceSlotItems(items, slotSelector));
      });
      return successCount.intValue();
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      this.slots.modifySlots(slot -> {
         SlotCollection mapped = this.mapper.apply(slot.get());
         mapped.modifySlots(consumer, slotSelector);
      });
   }
}
