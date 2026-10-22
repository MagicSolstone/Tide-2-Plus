package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import java.nio.ByteBuffer;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GLCapabilities;

@Environment(EnvType.CLIENT)
public abstract class BufferStorage {
   public static BufferStorage create(final GLCapabilities capabilities, final Set<String> enabledExtensions, final boolean forceMutable) {
      if (!forceMutable && capabilities.GL_ARB_buffer_storage && GlDevice.USE_GL_ARB_buffer_storage) {
         enabledExtensions.add("GL_ARB_buffer_storage");
         return new BufferStorage$Immutable();
      } else {
         return new BufferStorage$Mutable();
      }
   }

   public abstract GlBuffer createBuffer(GlHeuristics heuristics, DirectStateAccess dsa, @GpuBuffer$Usage int usage, long size);

   public abstract GlBuffer createBuffer(GlHeuristics heuristics, DirectStateAccess dsa, @GpuBuffer$Usage int usage, ByteBuffer data);
}
