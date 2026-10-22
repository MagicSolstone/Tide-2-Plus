package net.minecraft.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
interface BeaconScreen$BeaconButton {
   void updateStatus(final int levels);
}
