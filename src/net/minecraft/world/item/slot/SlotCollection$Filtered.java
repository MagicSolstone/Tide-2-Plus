package net.minecraft.world.item.slot;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;

public record SlotCollection$Filtered(SlotCollection slots, Predicate<? super ItemStack> filter) implements SlotCollection {
   @Override
   public Stream<ItemStack> itemCopies() {
      return this.slots.itemCopies().filter(this.filter);
   }

   @Override
   public int size() {
      return (int)this.itemCopies().count();
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      return this.slots.replaceSlotItems(items, slotSelector.filter(this.filter));
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      this.slots.modifySlots(consumer, slotSelector.filter(this.filter));
   }

   @Override
   public SlotCollection filter(final Predicate<? super ItemStack> predicate) {
      Objects.requireNonNull(predicate);
      return new SlotCollection$Filtered(this.slots, t -> this.filter.test(t) && predicate.test(t));
   }
}
