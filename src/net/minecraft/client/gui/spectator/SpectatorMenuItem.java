package net.minecraft.client.gui.spectator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public interface SpectatorMenuItem {
   void selectItem(SpectatorMenu menu);

   Component getName();

   void extractIcon(final GuiGraphicsExtractor graphics, float brightness, float alpha);

   boolean isEnabled();
}
