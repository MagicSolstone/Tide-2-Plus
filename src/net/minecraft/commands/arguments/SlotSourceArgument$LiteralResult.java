package net.minecraft.commands.arguments;

import java.util.Optional;
import net.minecraft.world.inventory.SlotRange;
import net.minecraft.world.item.slot.RangeSlotSource;
import net.minecraft.world.item.slot.SlotSource;

public record SlotSourceArgument$LiteralResult(SlotRange slotRange) implements SlotSourceArgument$Result {
   @Override
   public SlotSource value() {
      return RangeSlotSource.slotRange(this.slotRange);
   }

   @Override
   public Optional<String> name() {
      return Optional.of(this.slotRange.getSerializedName());
   }
}
