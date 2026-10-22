package net.minecraft.client.renderer.state.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum GuiRenderState$TraverseRange {
   ALL,
   BEFORE_BLUR,
   AFTER_BLUR;
}
