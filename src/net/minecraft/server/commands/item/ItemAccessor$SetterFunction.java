package net.minecraft.server.commands.item;

import net.minecraft.world.item.slot.SlotCollection;

@FunctionalInterface
public interface ItemAccessor$SetterFunction<Target> {
   int apply(Target target, SlotCollection slots);
}
