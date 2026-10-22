package com.mojang.renderpearl.backend.opengl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL33C;

@Environment(EnvType.CLIENT)
class GlStateManager$BooleanState {
   private final int state;
   private boolean enabled;

   public GlStateManager$BooleanState(final int state) {
      this.state = state;
   }

   public void disable() {
      this.setEnabled(false);
   }

   public void enable() {
      this.setEnabled(true);
   }

   public void setEnabled(final boolean enabled) {
      RenderSystem.assertOnRenderThread();
      if (enabled != this.enabled) {
         this.enabled = enabled;
         if (enabled) {
            GL33C.glEnable(this.state);
         } else {
            GL33C.glDisable(this.state);
         }
      }
   }
}
