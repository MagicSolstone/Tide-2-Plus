package net.minecraft.commands.arguments;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.item.slot.SlotSource;

public record SlotSourceArgument$HolderResult(Holder<SlotSource> holder) implements SlotSourceArgument$Result {
   @Override
   public SlotSource value() {
      return this.holder.value();
   }

   @Override
   public Optional<String> name() {
      return this.holder.getRegisteredNameIfPresent();
   }
}
