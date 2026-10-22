package com.mojang.blaze3d.resource;

import com.mojang.renderpearl.api.GpuFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record RenderTargetDescriptor$TextureProperties(@Nullable Vector4fc clearColor, GpuFormat format) {
   public static final RenderTargetDescriptor$TextureProperties DEFAULT_DEPTH = new RenderTargetDescriptor$TextureProperties(
      new Vector4f(0.0F, 0.0F, 0.0F, 0.0F), GpuFormat.D32_FLOAT
   );
}
