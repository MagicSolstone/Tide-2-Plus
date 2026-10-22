package com.mojang.renderpearl.backend.opengl;

import com.mojang.logging.LogUtils;
import java.util.Set;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.GLCapabilities;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public abstract class GlDebugLabel {
   private static final Logger LOGGER = LogUtils.getLogger();

   public void applyLabel(final GlBuffer buffer, @Nullable final Supplier<String> label) {
   }

   public void applyLabel(final GlTexture texture) {
   }

   public void applyLabel(final GlShaderModule shaderModule) {
   }

   public void applyLabel(final GlProgram program) {
   }

   public void pushDebugGroup(final Supplier<String> label) {
   }

   public void popDebugGroup() {
   }

   public static GlDebugLabel create(final GLCapabilities caps, final boolean wantsLabels, final Set<String> enabledExtensions) {
      if (wantsLabels) {
         if (caps.GL_KHR_debug && GlDevice.USE_GL_KHR_debug) {
            enabledExtensions.add("GL_KHR_debug");
            return new GlDebugLabel$Core();
         }

         if (caps.GL_EXT_debug_label && GlDevice.USE_GL_EXT_debug_label) {
            enabledExtensions.add("GL_EXT_debug_label");
            return new GlDebugLabel$Ext();
         }

         LOGGER.warn("Debug labels unavailable: neither KHR_debug nor EXT_debug_label are supported");
      }

      return new GlDebugLabel$Empty();
   }

   public boolean exists() {
      return false;
   }
}
