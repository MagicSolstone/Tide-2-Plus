package net.minecraft.world.inventory;

import java.util.List;

public class ItemCombinerMenuSlotDefinition {
   private final List<ItemCombinerMenuSlotDefinition$SlotDefinition> slots;
   private final ItemCombinerMenuSlotDefinition$SlotDefinition resultSlot;

   private ItemCombinerMenuSlotDefinition(
      final List<ItemCombinerMenuSlotDefinition$SlotDefinition> inputSlots, final ItemCombinerMenuSlotDefinition$SlotDefinition resultSlot
   ) {
      if (!inputSlots.isEmpty() && !resultSlot.equals(ItemCombinerMenuSlotDefinition$SlotDefinition.EMPTY)) {
         this.slots = inputSlots;
         this.resultSlot = resultSlot;
      } else {
         throw new IllegalArgumentException("Need to define both inputSlots and resultSlot");
      }
   }

   public static ItemCombinerMenuSlotDefinition$Builder create() {
      return new ItemCombinerMenuSlotDefinition$Builder();
   }

   public ItemCombinerMenuSlotDefinition$SlotDefinition getSlot(final int index) {
      return this.slots.get(index);
   }

   public ItemCombinerMenuSlotDefinition$SlotDefinition getResultSlot() {
      return this.resultSlot;
   }

   public List<ItemCombinerMenuSlotDefinition$SlotDefinition> getSlots() {
      return this.slots;
   }

   public int getNumOfInputSlots() {
      return this.slots.size();
   }

   public int getResultSlotIndex() {
      return this.getNumOfInputSlots();
   }
}
