package com.mojang.renderpearl.api.pipeline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.shaderc.ShadercIncludeResult;

@Environment(EnvType.CLIENT)
public record ShaderSource$CachedIncludeSource(long includeResultPtr) implements AutoCloseable {
   public static ShaderSource$CachedIncludeSource create(final Identifier id, final String source) {
      ShadercIncludeResult result = ShadercIncludeResult.calloc();
      result.source_name(MemoryUtil.memUTF8(id.toString(), false));
      result.content(MemoryUtil.memUTF8(source, false));
      return new ShaderSource$CachedIncludeSource(result.address());
   }

   public static ShaderSource$CachedIncludeSource createError(final String message) {
      ShadercIncludeResult result = ShadercIncludeResult.calloc();
      result.source_name(MemoryUtil.memUTF8("", false));
      result.content(MemoryUtil.memUTF8(message, false));
      return new ShaderSource$CachedIncludeSource(result.address());
   }

   @Override
   public void close() {
      MemoryUtil.nmemFree(MemoryUtil.memGetAddress(this.includeResultPtr + ShadercIncludeResult.SOURCE_NAME));
      MemoryUtil.nmemFree(MemoryUtil.memGetAddress(this.includeResultPtr + ShadercIncludeResult.CONTENT));
      MemoryUtil.nmemFree(this.includeResultPtr);
   }
}
