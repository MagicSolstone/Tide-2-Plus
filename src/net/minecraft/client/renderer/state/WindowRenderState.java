package net.minecraft.client.renderer.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WindowRenderState {
   public int width;
   public int height;
   public int guiScale;
   public float appropriateLineWidth;
   public boolean isMinimized;
}
