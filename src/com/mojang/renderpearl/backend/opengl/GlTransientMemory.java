package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.TransientMemory;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocation;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocator$CpuBlock;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public abstract class GlTransientMemory implements TransientMemory, UncheckedAutoCloseable {
   private static final long BLOCK_SIZE = 524288L;
   private static final long MAX_CPU_ALIGNMENT = 16L;
   private static final long MAX_GPU_ALIGNMENT = Long.highestOneBit(Long.MAX_VALUE);
   final GlCommandEncoder encoder;
   protected final DirectStateAccess dsa;
   protected final BufferStorage bufferStorage;
   protected final GlDebugLabel debugLabels;
   protected final GlHeuristics heuristics;
   private final TransientBlockAllocator<TransientBlockAllocator$Allocator$CpuBlock> cpuBlockAllocator = new TransientBlockAllocator<>(
      524288L, 16L, TransientBlockAllocator$Allocator$CpuBlock.memalloc()
   );

   GlTransientMemory(final GlDevice device, final GlCommandEncoder encoder) {
      this.encoder = encoder;
      this.dsa = device.directStateAccess();
      this.bufferStorage = device.getBufferStorage();
      this.debugLabels = device.debugLabels();
      this.heuristics = device.heuristics();
   }

   @Override
   public void close() {
   }

   @Override
   public ByteBuffer allocateCpu(final long size, final long alignment, final long minimumAllocation, final long elementSize) {
      assert size <= 2147483647L;

      TransientBlockAllocator$Allocation<TransientBlockAllocator$Allocator$CpuBlock> alloc = this.cpuBlockAllocator
         .allocate(size, alignment, minimumAllocation, elementSize);
      return MemoryUtil.memByteBuffer(alloc.block().address() + alloc.offset(), (int)alloc.size());
   }

   public void rotate() {
      this.cpuBlockAllocator.rotate().run();
   }
}
