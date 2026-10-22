package net.minecraft.world.level.storage.loot;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ContainerComponent;
import net.minecraft.world.item.slot.SlotCollection;

public record ContainerComponentManipulator<T extends ContainerComponent<T>>(DataComponentType<T> type, T empty) {
   public void setContents(final ItemStack itemStack, final T defaultValue, final Stream<ItemStack> newContents) {
      T currentValue = itemStack.getOrDefault(this.type(), defaultValue);
      T newValue = currentValue.copyWithContents(newContents);
      itemStack.set(this.type(), newValue);
   }

   public void setContents(final ItemStack itemStack, final Stream<ItemStack> newContents) {
      this.setContents(itemStack, this.empty(), newContents);
   }

   public void modifyItems(final ItemStack itemStack, final UnaryOperator<ItemStack> modifier) {
      T contents = itemStack.get(this.type());
      if (contents != null) {
         UnaryOperator<ItemStack> nonEmptyModifier = currentItemStack -> {
            if (currentItemStack.isEmpty()) {
               return currentItemStack;
            } else {
               ItemStack newItemStack = modifier.apply(currentItemStack);
               newItemStack.limitSize(newItemStack.getMaxStackSize());
               return newItemStack;
            }
         };
         this.setContents(itemStack, contents.itemCopies().map(nonEmptyModifier));
      }
   }

   public SlotCollection getSlots(final ItemStack itemStack) {
      return new ContainerComponentManipulator$ComponentSlotCollection<>(itemStack, this);
   }
}
