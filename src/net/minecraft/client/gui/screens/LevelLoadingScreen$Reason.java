package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum LevelLoadingScreen$Reason {
   NETHER_PORTAL,
   END_PORTAL,
   OTHER;
}
