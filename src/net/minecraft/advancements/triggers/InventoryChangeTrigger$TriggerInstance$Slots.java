package net.minecraft.advancements.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.MinMaxBounds$Ints;

public record InventoryChangeTrigger$TriggerInstance$Slots(MinMaxBounds$Ints occupied, MinMaxBounds$Ints full, MinMaxBounds$Ints empty) {
   public static final Codec<InventoryChangeTrigger$TriggerInstance$Slots> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            MinMaxBounds$Ints.CODEC.optionalFieldOf("occupied", MinMaxBounds$Ints.ANY).forGetter(InventoryChangeTrigger$TriggerInstance$Slots::occupied),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("full", MinMaxBounds$Ints.ANY).forGetter(InventoryChangeTrigger$TriggerInstance$Slots::full),
            MinMaxBounds$Ints.CODEC.optionalFieldOf("empty", MinMaxBounds$Ints.ANY).forGetter(InventoryChangeTrigger$TriggerInstance$Slots::empty)
         )
         .apply(i, InventoryChangeTrigger$TriggerInstance$Slots::new)
   );
   public static final InventoryChangeTrigger$TriggerInstance$Slots ANY = new InventoryChangeTrigger$TriggerInstance$Slots(
      MinMaxBounds$Ints.ANY, MinMaxBounds$Ints.ANY, MinMaxBounds$Ints.ANY
   );

   public boolean matches(final int slotsFull, final int slotsEmpty, final int slotsOccupied) {
      if (!this.full.matches(slotsFull)) {
         return false;
      } else {
         return !this.empty.matches(slotsEmpty) ? false : this.occupied.matches(slotsOccupied);
      }
   }
}
