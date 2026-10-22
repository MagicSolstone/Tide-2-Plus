package com.mojang.renderpearl.backend.opengl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GlStateManager$DepthState {
   public final GlStateManager$BooleanState mode = new GlStateManager$BooleanState(2929);
   public boolean mask = true;
   public int func = 513;

   private GlStateManager$DepthState() {
   }
}
