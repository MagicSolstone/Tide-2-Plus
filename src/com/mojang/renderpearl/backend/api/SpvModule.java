package com.mojang.renderpearl.backend.api;

import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.util.ShaderCompileException;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface SpvModule extends AutoCloseable {
   @Override
   void close();

   ByteBuffer spv();

   ShaderType type();

   SpvModule$Reflection reflect() throws ShaderCompileException;

   @Nullable
   SpvModule$Reflection getReflectionInfoIfAvailable();
}
