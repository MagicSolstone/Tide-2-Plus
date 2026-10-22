package net.minecraft.client.gui.screens.debug;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ContainerObjectSelectionList$Entry;

@Environment(EnvType.CLIENT)
public abstract class DebugOptionsScreen$AbstractOptionEntry extends ContainerObjectSelectionList$Entry<DebugOptionsScreen$AbstractOptionEntry> {
   public abstract void refreshEntry();
}
