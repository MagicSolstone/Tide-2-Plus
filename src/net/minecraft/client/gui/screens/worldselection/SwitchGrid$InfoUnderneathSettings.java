package net.minecraft.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record SwitchGrid$InfoUnderneathSettings(int maxInfoRows, boolean alwaysMaxHeight) {
}
