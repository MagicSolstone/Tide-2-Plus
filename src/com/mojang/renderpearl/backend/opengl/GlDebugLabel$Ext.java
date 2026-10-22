package com.mojang.renderpearl.backend.opengl;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringUtil;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.EXTDebugLabel;

@Environment(EnvType.CLIENT)
class GlDebugLabel$Ext extends GlDebugLabel {
   private GlDebugLabel$Ext() {
   }

   @Override
   public void applyLabel(final GlBuffer buffer, @Nullable final Supplier<String> label) {
      if (label != null) {
         EXTDebugLabel.glLabelObjectEXT(37201, buffer.handle(), StringUtil.truncateStringIfNecessary(label.get(), 256, true));
      }
   }

   @Override
   public void applyLabel(final GlTexture texture) {
      EXTDebugLabel.glLabelObjectEXT(5890, texture.id, StringUtil.truncateStringIfNecessary(texture.getLabel(), 256, true));
   }

   @Override
   public void applyLabel(final GlShaderModule shaderModule) {
      EXTDebugLabel.glLabelObjectEXT(35656, shaderModule.getShaderId(), StringUtil.truncateStringIfNecessary(shaderModule.getLabel(), 256, true));
   }

   @Override
   public void applyLabel(final GlProgram program) {
      EXTDebugLabel.glLabelObjectEXT(35648, program.getProgramId(), StringUtil.truncateStringIfNecessary(program.getDebugLabel(), 256, true));
   }

   @Override
   public boolean exists() {
      return true;
   }
}
