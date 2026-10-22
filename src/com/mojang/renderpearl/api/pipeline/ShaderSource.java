package com.mojang.renderpearl.api.pipeline;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ShaderSource extends AutoCloseable {
   @Nullable
   String getShader(Identifier id, ShaderType type);

   @Nullable
   ShaderSource$CachedIncludeSource getInclude(Identifier id);

   @Override
   void close();
}
