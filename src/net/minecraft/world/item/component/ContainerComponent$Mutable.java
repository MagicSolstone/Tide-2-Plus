package net.minecraft.world.item.component;

import net.minecraft.world.item.slot.SlotCollection;

public interface ContainerComponent$Mutable<T> extends SlotCollection {
   T toImmutable();
}
