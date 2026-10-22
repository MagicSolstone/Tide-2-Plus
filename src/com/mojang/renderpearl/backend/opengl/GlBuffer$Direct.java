package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public class GlBuffer$Direct extends GlBuffer {
   private boolean closed;
   private final DirectStateAccess dsa;
   protected final int mappingFlags;
   @Nullable
   protected ByteBuffer mappedBuffer;

   protected GlBuffer$Direct(
      final GlHeuristics heuristics,
      final DirectStateAccess dsa,
      @GpuBuffer$Usage final int usage,
      final long size,
      final int handle,
      final boolean canPersistentMap
   ) {
      this.dsa = dsa;
      int clampedSize = (int)Math.min(size, 2147483647L);
      MEMORY_POOL.malloc(handle, clampedSize);
      int mappingFlags = 0;
      if ((usage & 1) != 0) {
         mappingFlags |= 1;
      }

      if ((usage & 2) != 0) {
         mappingFlags |= 2;
         if (!heuristics.isGlOnDx12()) {
            mappingFlags |= 32;
         }
      }

      if (canPersistentMap) {
         mappingFlags |= 192;
      }

      this.mappingFlags = mappingFlags;
      super(usage, size, handle, canPersistentMap);
      if (canPersistentMap && (usage & 3) != 0) {
         this.map(0L, size, (usage & 1) != 0, (usage & 2) != 0);
      }
   }

   @Override
   public boolean isClosed() {
      return this.closed;
   }

   @Override
   public void close() {
      if (!this.closed) {
         this.closed = true;
         if (this.canPersistentMap && (this.usage() & 3) != 0) {
            this.unmap();
         }

         if (this.mappingRefCount != 0) {
            throw new IllegalStateException("Attempt to close a mapped buffer");
         } else {
            GlStateManager._glDeleteBuffers(this.handle());
            MEMORY_POOL.free(this.handle());
         }
      }
   }

   @Override
   public GpuBufferSlice$MappedView map(final long offset, final long length, final boolean read, final boolean write) {
      if (this.isClosed()) {
         throw new IllegalStateException("Buffer already closed");
      } else if (!read && !write) {
         throw new IllegalArgumentException("At least read or write must be true");
      } else if (read && (this.usage() & 1) == 0) {
         throw new IllegalStateException("Buffer is not readable");
      } else if (write && (this.usage() & 2) == 0) {
         throw new IllegalStateException("Buffer is not writable");
      } else if (offset + length > this.size()) {
         throw new IllegalArgumentException(
            "Cannot map more data than this buffer can hold (attempting to map "
               + length
               + " bytes at offset "
               + offset
               + " from "
               + this.size()
               + " size buffer)"
         );
      } else if (offset > 2147483647L || length > 2147483647L) {
         throw new IllegalArgumentException("Mapping buffers larger than 2GB is not supported");
      } else if (offset >= 0L && length >= 0L) {
         this.mappingRefCount++;
         if (this.mappedBuffer == null) {
            GlStateManager.clearGlErrors();
            this.mappedBuffer = this.dsa.mapBufferRange(this.handle(), 0L, this.size(), this.mappingFlags, this.usage());
            if (this.mappedBuffer == null) {
               throw new IllegalStateException("Failed to map buffer");
            }
         }

         return new GpuBufferSlice$MappedView(
            this.slice(offset, length), MemoryUtil.memSlice(this.mappedBuffer, (int)offset, (int)length), new GlBuffer$Direct$1(this)
         );
      } else {
         throw new IllegalArgumentException("Offset or length must be positive integer values");
      }
   }

   private void unmap() {
      this.mappingRefCount--;
      if (this.mappingRefCount == 0) {
         this.dsa.unmapBuffer(this.handle(), this.usage());
         this.mappedBuffer = null;
      }
   }
}
