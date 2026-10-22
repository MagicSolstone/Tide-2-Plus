package net.minecraft.commands.arguments;

import java.util.Optional;
import net.minecraft.world.item.slot.SlotSource;

public sealed interface SlotSourceArgument$Result permits SlotSourceArgument$HolderResult, SlotSourceArgument$LiteralResult {
   SlotSource value();

   Optional<String> name();
}
