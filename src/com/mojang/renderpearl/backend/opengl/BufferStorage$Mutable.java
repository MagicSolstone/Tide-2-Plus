package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class BufferStorage$Mutable extends BufferStorage {
   private BufferStorage$Mutable() {
   }

   @Override
   public GlBuffer createBuffer(final GlHeuristics heuristics, final DirectStateAccess dsa, @GpuBuffer$Usage final int usage, final long size) {
      int buffer = dsa.createBuffer();
      dsa.bufferData(buffer, size, usage);
      return new GlBuffer$Direct(heuristics, dsa, usage, size, buffer, false);
   }

   @Override
   public GlBuffer createBuffer(final GlHeuristics heuristics, final DirectStateAccess dsa, @GpuBuffer$Usage final int usage, final ByteBuffer data) {
      int buffer = dsa.createBuffer();
      int size = data.remaining();
      dsa.bufferData(buffer, data, usage);
      return new GlBuffer$Direct(heuristics, dsa, usage, size, buffer, false);
   }
}
