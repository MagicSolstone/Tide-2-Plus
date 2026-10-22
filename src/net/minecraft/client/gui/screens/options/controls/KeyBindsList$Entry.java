package net.minecraft.client.gui.screens.options.controls;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ContainerObjectSelectionList$Entry;

@Environment(EnvType.CLIENT)
public abstract class KeyBindsList$Entry extends ContainerObjectSelectionList$Entry<KeyBindsList$Entry> {
   public abstract void refreshEntry();
}
