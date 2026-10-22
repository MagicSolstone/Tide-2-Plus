package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@Environment(EnvType.CLIENT)
interface MenuScreens$ScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
   default void fromPacket(final Component title, final MenuType<T> type, final Minecraft minecraft, final int containerId) {
      U screen = this.create(type.create(containerId, minecraft.player.getInventory()), minecraft.player.getInventory(), title);
      minecraft.player.containerMenu = screen.getMenu();
      minecraft.gui.setScreen(screen);
   }

   U create(T menu, Inventory inventory, final Component title);
}
