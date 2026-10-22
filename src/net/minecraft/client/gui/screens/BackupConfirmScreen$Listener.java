package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface BackupConfirmScreen$Listener {
   void proceed(final boolean backup, final boolean eraseCache);
}
