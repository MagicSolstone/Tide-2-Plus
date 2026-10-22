package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class BufferStorage$Immutable extends BufferStorage {
   private BufferStorage$Immutable() {
   }

   @Override
   public GlBuffer createBuffer(final GlHeuristics heuristics, final DirectStateAccess dsa, @GpuBuffer$Usage final int usage, final long size) {
      int buffer = dsa.createBuffer();
      dsa.bufferStorage(buffer, size, usage);
      return new GlBuffer$Direct(heuristics, dsa, usage, size, buffer, true);
   }

   @Override
   public GlBuffer createBuffer(final GlHeuristics heuristics, final DirectStateAccess dsa, @GpuBuffer$Usage final int usage, final ByteBuffer data) {
      int buffer = dsa.createBuffer();
      int size = data.remaining();
      dsa.bufferStorage(buffer, data, usage);
      return new GlBuffer$Direct(heuristics, dsa, usage, size, buffer, true);
   }
}
