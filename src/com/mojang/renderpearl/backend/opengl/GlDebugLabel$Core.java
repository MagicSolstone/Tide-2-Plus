package com.mojang.renderpearl.backend.opengl;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringUtil;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.KHRDebug;

@Environment(EnvType.CLIENT)
class GlDebugLabel$Core extends GlDebugLabel {
   private final int maxLabelLength = GL33C.glGetInteger(33512);

   private GlDebugLabel$Core() {
   }

   @Override
   public void applyLabel(final GlBuffer buffer, @Nullable final Supplier<String> label) {
      if (label != null) {
         KHRDebug.glObjectLabel(33504, buffer.handle(), StringUtil.truncateStringIfNecessary(label.get(), this.maxLabelLength, true));
      }
   }

   @Override
   public void applyLabel(final GlTexture texture) {
      KHRDebug.glObjectLabel(5890, texture.id, StringUtil.truncateStringIfNecessary(texture.getLabel(), this.maxLabelLength, true));
   }

   @Override
   public void applyLabel(final GlShaderModule shaderModule) {
      KHRDebug.glObjectLabel(33505, shaderModule.getShaderId(), StringUtil.truncateStringIfNecessary(shaderModule.getLabel(), this.maxLabelLength, true));
   }

   @Override
   public void applyLabel(final GlProgram program) {
      KHRDebug.glObjectLabel(33506, program.getProgramId(), StringUtil.truncateStringIfNecessary(program.getDebugLabel(), this.maxLabelLength, true));
   }

   @Override
   public void pushDebugGroup(final Supplier<String> label) {
      KHRDebug.glPushDebugGroup(33354, 0, label.get());
   }

   @Override
   public void popDebugGroup() {
      KHRDebug.glPopDebugGroup();
   }

   @Override
   public boolean exists() {
      return true;
   }
}
