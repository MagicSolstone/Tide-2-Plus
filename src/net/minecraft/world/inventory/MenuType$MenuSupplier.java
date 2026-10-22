package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Inventory;

interface MenuType$MenuSupplier<T extends AbstractContainerMenu> {
   T create(int containerId, Inventory inventory);
}
