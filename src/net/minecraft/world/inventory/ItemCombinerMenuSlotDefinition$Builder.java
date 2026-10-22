package net.minecraft.world.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

public class ItemCombinerMenuSlotDefinition$Builder {
   private final List<ItemCombinerMenuSlotDefinition$SlotDefinition> inputSlots = new ArrayList<>();
   private ItemCombinerMenuSlotDefinition$SlotDefinition resultSlot = ItemCombinerMenuSlotDefinition$SlotDefinition.EMPTY;

   public ItemCombinerMenuSlotDefinition$Builder withSlot(final int slotIndex, final int xPlacement, final int yPlacement, final Predicate<ItemStack> mayPlace) {
      this.inputSlots.add(new ItemCombinerMenuSlotDefinition$SlotDefinition(slotIndex, xPlacement, yPlacement, mayPlace));
      return this;
   }

   public ItemCombinerMenuSlotDefinition$Builder withResultSlot(final int slotIndex, final int xPlacement, final int yPlacement) {
      this.resultSlot = new ItemCombinerMenuSlotDefinition$SlotDefinition(slotIndex, xPlacement, yPlacement, itemStack -> false);
      return this;
   }

   public ItemCombinerMenuSlotDefinition build() {
      int inputCount = this.inputSlots.size();

      for (int i = 0; i < inputCount; i++) {
         ItemCombinerMenuSlotDefinition$SlotDefinition inputDefinition = this.inputSlots.get(i);
         if (inputDefinition.slotIndex != i) {
            throw new IllegalArgumentException("Expected input slots to have continous indexes");
         }
      }

      if (this.resultSlot.slotIndex != inputCount) {
         throw new IllegalArgumentException("Expected result slot index to follow last input slot");
      } else {
         return new ItemCombinerMenuSlotDefinition(this.inputSlots, this.resultSlot);
      }
   }
}
