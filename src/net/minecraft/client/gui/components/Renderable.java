package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;

@Environment(EnvType.CLIENT)
public interface Renderable {
   void extractRenderState(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, final float a);
}
