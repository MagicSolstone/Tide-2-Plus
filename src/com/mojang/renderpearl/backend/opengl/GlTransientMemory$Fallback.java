package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocation;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
class GlTransientMemory$Fallback extends GlTransientMemory {
   private final TransientBlockAllocator<GlTransientMemory$Fallback$GlAllocation> blockAllocator = new TransientBlockAllocator<>(
      524288L, GlTransientMemory.MAX_GPU_ALIGNMENT, TransientBlockAllocator$Allocator.create(this::allocateGlBlock, this::freeGlBlock)
   );

   GlTransientMemory$Fallback(final GlDevice device, final GlCommandEncoder encoder) {
      super(device, encoder);
   }

   @Override
   public void close() {
      this.rotate();
   }

   @Override
   public void rotate() {
      this.blockAllocator.rotate().run();
   }

   private GlTransientMemory$Fallback$GlAllocation allocateGlBlock(final long size) {
      GlBuffer buffer = this.bufferStorage.createBuffer(this.heuristics, this.dsa, 40, size);
      this.debugLabels.applyLabel(buffer, () -> "OpenGL Transient Buffer");
      long hostPtr = MemoryUtil.nmemAlloc(size);
      return new GlTransientMemory$Fallback$GlAllocation(buffer, hostPtr);
   }

   private void freeGlBlock(final GlTransientMemory$Fallback$GlAllocation allocation) {
      allocation.glBuffer.close();
      MemoryUtil.nmemFree(allocation.hostBuffer);
   }

   @Override
   public GpuBufferSlice$MappedView allocateStaging(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      TransientBlockAllocator$Allocation<GlTransientMemory$Fallback$GlAllocation> allocation = this.blockAllocator
         .allocate(size, alignment, minimumAllocation, elementSize);
      GlTransientMemory$TransientGpuBuffer transientBuffer = new GlTransientMemory$TransientGpuBuffer(
         this, allocation.block().glBuffer().handle(), usage, allocation.block().glBuffer().size()
      );
      GpuBufferSlice slice = new GpuBufferSlice(transientBuffer, allocation.offset(), allocation.size());
      ByteBuffer hostBuffer = MemoryUtil.memByteBuffer(allocation.block().hostBuffer + allocation.offset(), (int)allocation.size());
      return new GpuBufferSlice$MappedView(
         slice,
         hostBuffer,
         () -> this.dsa
            .bufferSubData(
               transientBuffer.handle(),
               slice.offset(),
               MemoryUtil.memByteBuffer(allocation.block().hostBuffer + allocation.offset(), (int)allocation.size()),
               usage
            )
      );
   }

   @Override
   public GpuBufferSlice allocateGpu(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      TransientBlockAllocator$Allocation<GlTransientMemory$Fallback$GlAllocation> allocation = this.blockAllocator
         .allocate(size, alignment, minimumAllocation, elementSize);
      GlTransientMemory$TransientGpuBuffer transientBuffer = new GlTransientMemory$TransientGpuBuffer(
         this, allocation.block().glBuffer().handle(), usage, allocation.block().glBuffer().size()
      );
      return new GpuBufferSlice(transientBuffer, allocation.offset(), allocation.size());
   }

   @Override
   public GpuBufferSlice$MappedView allocateGpuMapped(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      return this.allocateStaging(size, alignment, usage, minimumAllocation, elementSize);
   }

   @Override
   public GpuBufferSlice uploadStaging(
      final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      return this.uploadGpu(data, alignment, usage, minimumAllocation, elementSize);
   }

   @Override
   public GpuBufferSlice uploadGpu(
      final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      long totalSize = 0L;

      for (int i = 0; i < data.size(); i++) {
         totalSize += data.get(i).remaining();
         totalSize = Mth.roundToward(totalSize, alignment);
      }

      GpuBufferSlice bufferSlice = this.allocateGpu(totalSize, alignment, usage);
      int target = GlUtil.selectBufferBindTarget(usage);
      GlStateManager._glBindBuffer(target, ((GlBuffer)bufferSlice.buffer()).handle());
      long ptr = GL33C.nglMapBufferRange(target, bufferSlice.offset(), totalSize, 38);
      long offset = 0L;

      for (int i = 0; i < data.size(); i++) {
         ByteBuffer buffer = data.get(i);
         MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), ptr + offset, buffer.remaining());
         offset += buffer.remaining();
         offset = Mth.roundToward(offset, alignment);
      }

      GL33C.glUnmapBuffer(target);
      GlStateManager._glBindBuffer(target, 0);
      return bufferSlice;
   }

   @Override
   public List<GpuBufferSlice> multiUploadStaging(final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage) {
      return this.multiUploadGpu(data, alignment, usage);
   }

   @Override
   public List<GpuBufferSlice> multiUploadGpu(final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage) {
      ReferenceArrayList<GpuBufferSlice> uploadedBuffers = new ReferenceArrayList();
      uploadedBuffers.size(data.size());
      IntArrayList sortedDataIndices = IntArrayList.toList(IntStream.range(0, data.size()));
      sortedDataIndices.sort(IntComparator.comparing(index -> data.get(index).remaining()));
      int target = GlUtil.selectBufferBindTarget(usage);

      while (!sortedDataIndices.isEmpty()) {
         boolean allocatedAnything = false;

         for (int i = sortedDataIndices.size() - 1; i >= 0; i--) {
            int bufferIndex = sortedDataIndices.getInt(i);
            ByteBuffer currentBuffer = data.get(bufferIndex);
            if (this.blockAllocator.canAllocateInCurrentBlock(currentBuffer.remaining(), alignment)) {
               sortedDataIndices.removeInt(i);
               GpuBufferSlice bufferSlice = this.allocateGpu(currentBuffer.remaining(), alignment, usage);
               uploadedBuffers.set(bufferIndex, bufferSlice);
               GlStateManager._glBindBuffer(target, ((GlBuffer)bufferSlice.buffer()).handle());
               long ptr = GL33C.nglMapBufferRange(target, bufferSlice.offset(), bufferSlice.length(), 38);
               MemoryUtil.memCopy(MemoryUtil.memAddress(currentBuffer), ptr, bufferSlice.length());
               GL33C.glUnmapBuffer(target);
               allocatedAnything = true;
               break;
            }
         }

         if (!allocatedAnything) {
            int bufferIndex = sortedDataIndices.popInt();
            ByteBuffer currentBuffer = data.get(bufferIndex);
            GpuBufferSlice bufferSlice = this.allocateGpu(currentBuffer.remaining(), alignment, usage);
            uploadedBuffers.set(bufferIndex, bufferSlice);
            GlStateManager._glBindBuffer(target, ((GlBuffer)bufferSlice.buffer()).handle());
            long ptr = GL33C.nglMapBufferRange(target, bufferSlice.offset(), bufferSlice.length(), 38);
            MemoryUtil.memCopy(MemoryUtil.memAddress(currentBuffer), ptr, bufferSlice.length());
            GL33C.glUnmapBuffer(target);
         }
      }

      GlStateManager._glBindBuffer(target, 0);
      return uploadedBuffers;
   }
}
