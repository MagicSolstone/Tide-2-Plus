package com.mojang.renderpearl.backend.opengl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GlStateManager$PolygonOffsetState {
   public final GlStateManager$BooleanState fill = new GlStateManager$BooleanState(32823);
   public float factor;
   public float units;

   private GlStateManager$PolygonOffsetState() {
   }
}
