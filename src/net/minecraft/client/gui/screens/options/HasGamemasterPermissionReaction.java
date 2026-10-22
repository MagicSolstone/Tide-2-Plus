package net.minecraft.client.gui.screens.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface HasGamemasterPermissionReaction {
   void onGamemasterPermissionChanged(final boolean hasGamemasterPermission);
}
