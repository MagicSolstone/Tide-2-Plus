package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum Hud$ContextualInfo {
   EMPTY,
   EXPERIENCE,
   LOCATOR,
   JUMPABLE_VEHICLE;
}
