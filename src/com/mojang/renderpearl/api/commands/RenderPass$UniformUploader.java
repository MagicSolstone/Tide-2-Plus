package com.mojang.renderpearl.api.commands;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface RenderPass$UniformUploader {
   void setUniform(String name, GpuBufferSlice buffer);

   void pushConstants(ByteBuffer buffer);
}
