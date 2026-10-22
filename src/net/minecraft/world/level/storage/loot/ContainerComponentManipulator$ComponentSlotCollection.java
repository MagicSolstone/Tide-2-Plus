package net.minecraft.world.level.storage.loot;

import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ContainerComponent;
import net.minecraft.world.item.component.ContainerComponent$Mutable;
import net.minecraft.world.item.slot.SlotCollection;
import net.minecraft.world.item.slot.SlotSelector;

record ContainerComponentManipulator$ComponentSlotCollection<T extends ContainerComponent<T>>(ItemStack itemStack, ContainerComponentManipulator<T> component)
   implements SlotCollection {
   @Override
   public Stream<ItemStack> itemCopies() {
      T contents = this.itemStack.get(this.component.type());
      return contents != null ? contents.itemCopies() : Stream.empty();
   }

   @Override
   public int size() {
      T contents = this.itemStack.get(this.component.type());
      return contents != null ? contents.size() : 0;
   }

   @Override
   public int replaceSlotItems(final ItemProvider items, final SlotSelector slotSelector) {
      T currentValue = this.itemStack.getOrDefault(this.component.type(), this.component.empty());
      ContainerComponent$Mutable<T> mutable = currentValue.asMutable();
      int slotsReplaced = mutable.replaceSlotItems(items, slotSelector);
      this.itemStack.set(this.component.type(), mutable.toImmutable());
      return slotsReplaced;
   }

   @Override
   public void modifySlots(final Consumer<? super SlotAccess> consumer, final SlotSelector slotSelector) {
      T currentValue = this.itemStack.getOrDefault(this.component.type(), this.component.empty());
      ContainerComponent$Mutable<T> mutable = currentValue.asMutable();
      mutable.modifySlots(consumer, slotSelector);
      this.itemStack.set(this.component.type(), mutable.toImmutable());
   }
}
