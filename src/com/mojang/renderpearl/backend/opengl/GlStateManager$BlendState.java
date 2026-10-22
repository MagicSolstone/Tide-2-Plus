package com.mojang.renderpearl.backend.opengl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GlStateManager$BlendState {
   public int srcRgb = 1;
   public int dstRgb = 0;
   public int modeRgb = 32774;
   public int srcAlpha = 1;
   public int dstAlpha = 0;
   public int modeAlpha = 32774;

   private GlStateManager$BlendState() {
   }
}
