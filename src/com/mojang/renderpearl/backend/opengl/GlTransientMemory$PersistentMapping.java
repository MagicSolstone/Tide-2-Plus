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
import org.lwjgl.opengl.ARBBufferStorage;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
class GlTransientMemory$PersistentMapping extends GlTransientMemory {
   private final TransientBlockAllocator<GlTransientMemory$PersistentMapping$GlAllocation> stagingBlockAllocator;
   private final TransientBlockAllocator<GlTransientMemory$PersistentMapping$GlAllocation> gpuBlockAllocator;
   private final TransientBlockAllocator<GlTransientMemory$PersistentMapping$GlAllocation> gpuMappedBlockAllocator;
   private final GlTransientMemory$PersistentMapping$Rotation[] rotations = new GlTransientMemory$PersistentMapping$Rotation[2];

   GlTransientMemory$PersistentMapping(final GlDevice device, final GlCommandEncoder encoder) {
      super(device, encoder);
      this.stagingBlockAllocator = new TransientBlockAllocator<>(
         524288L,
         GlTransientMemory.MAX_GPU_ALIGNMENT,
         TransientBlockAllocator$Allocator.create(size -> this.allocateGlBlock(size, true, true), this::freeGlBlock)
      );
      this.gpuBlockAllocator = new TransientBlockAllocator<>(
         524288L,
         GlTransientMemory.MAX_GPU_ALIGNMENT,
         TransientBlockAllocator$Allocator.create(size -> this.allocateGlBlock(size, false, false), this::freeGlBlock)
      );
      this.gpuMappedBlockAllocator = new TransientBlockAllocator<>(
         524288L,
         GlTransientMemory.MAX_GPU_ALIGNMENT,
         TransientBlockAllocator$Allocator.create(size -> this.allocateGlBlock(size, false, true), this::freeGlBlock)
      );
   }

   @Override
   public void close() {
      GL33C.glFinish();

      for (GlTransientMemory$PersistentMapping$Rotation rotation : this.rotations) {
         if (rotation != null) {
            rotation.run();
         }
      }
   }

   @Override
   public void rotate() {
      GlTransientMemory$PersistentMapping$Rotation previousRotation = this.rotations[this.encoder.currentSubmitSlot()];
      this.rotations[this.encoder.currentSubmitSlot()] = new GlTransientMemory$PersistentMapping$Rotation(
         this.stagingBlockAllocator.rotate(), this.gpuBlockAllocator.rotate(), this.gpuMappedBlockAllocator.rotate()
      );
      if (previousRotation != null) {
         previousRotation.run();
      }

      super.rotate();
   }

   private GlTransientMemory$PersistentMapping$GlAllocation allocateGlBlock(final long size, final boolean host, final boolean mapped) {
      int glBuffer = this.dsa.createBuffer();
      int usageFlags = 0;
      if (host) {
         usageFlags |= 512;
      }

      if (mapped) {
         usageFlags |= 2;
         usageFlags |= 64;
         usageFlags |= 128;
      }

      GlStateManager._glBindBuffer(34962, glBuffer);
      ARBBufferStorage.glBufferStorage(34962, size, usageFlags);
      long hostPtr;
      if (mapped) {
         hostPtr = GL33C.nglMapBufferRange(34962, 0L, size, 226);
      } else {
         hostPtr = 0L;
      }

      GlStateManager._glBindBuffer(34962, 0);
      this.debugLabels.applyLabel(new GlTransientMemory$TransientGpuBuffer(this, glBuffer, 0, size), () -> "OpenGL Transient Buffer");
      return new GlTransientMemory$PersistentMapping$GlAllocation(glBuffer, hostPtr, size);
   }

   private void freeGlBlock(final GlTransientMemory$PersistentMapping$GlAllocation allocation) {
      GlStateManager._glDeleteBuffers(allocation.glBuffer);
   }

   @Override
   public GpuBufferSlice$MappedView allocateStaging(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      if (!$assertionsDisabled && size > 2147483647L) {
         throw new AssertionError();
      } else {
         TransientBlockAllocator$Allocation<GlTransientMemory$PersistentMapping$GlAllocation> alloc = this.stagingBlockAllocator
            .allocate(size, alignment, minimumAllocation, elementSize);
         GlTransientMemory$TransientGpuBuffer apiBuffer = new GlTransientMemory$TransientGpuBuffer(this, alloc.block().glBuffer, usage, (int)alloc.block().size);
         ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(alloc.block().hostPtr + alloc.offset(), (int)alloc.size());
         return new GpuBufferSlice$MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {});
      }
   }

   @Override
   public GpuBufferSlice allocateGpu(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      if (!$assertionsDisabled && size > 2147483647L) {
         throw new AssertionError();
      } else {
         TransientBlockAllocator$Allocation<GlTransientMemory$PersistentMapping$GlAllocation> alloc = this.gpuBlockAllocator
            .allocate(size, alignment, minimumAllocation, elementSize);
         GlTransientMemory$TransientGpuBuffer apiBuffer = new GlTransientMemory$TransientGpuBuffer(this, alloc.block().glBuffer, usage, (int)alloc.block().size);
         return new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size());
      }
   }

   @Override
   public GpuBufferSlice$MappedView allocateGpuMapped(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      if (!$assertionsDisabled && size > 2147483647L) {
         throw new AssertionError();
      } else {
         TransientBlockAllocator$Allocation<GlTransientMemory$PersistentMapping$GlAllocation> alloc = this.gpuMappedBlockAllocator
            .allocate(size, alignment, minimumAllocation, elementSize);
         GlTransientMemory$TransientGpuBuffer apiBuffer = new GlTransientMemory$TransientGpuBuffer(this, alloc.block().glBuffer, usage, (int)alloc.block().size);
         ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(alloc.block().hostPtr + alloc.offset(), (int)alloc.size());
         return new GpuBufferSlice$MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {});
      }
   }

   @Override
   public GpuBufferSlice uploadStaging(
      final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      return this.upload(data, alignment, usage, minimumAllocation, elementSize, true);
   }

   @Override
   public GpuBufferSlice uploadGpu(
      final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      return this.upload(data, alignment, usage, minimumAllocation, elementSize, false);
   }

   public GpuBufferSlice upload(
      final List<ByteBuffer> data,
      final long alignment,
      @GpuBuffer$Usage final int usage,
      final long minimumAllocation,
      final long elementSize,
      final boolean staging
   ) {
      long totalSize = 0L;

      for (ByteBuffer buffer : data) {
         totalSize += buffer.remaining();
         totalSize = Mth.roundToward(totalSize, alignment);
      }

      GpuBufferSlice var25;
      try (GpuBufferSlice$MappedView mapped = staging
            ? this.allocateStaging(totalSize, alignment, usage, minimumAllocation, elementSize)
            : this.allocateGpuMapped(totalSize, alignment, usage, minimumAllocation, elementSize)) {
         long mappedPtr = MemoryUtil.memAddress(mapped.data());
         long offset = 0L;

         for (ByteBuffer buffer : data) {
            MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), mappedPtr + offset, Math.min(mapped.slice().length() - offset, (long)buffer.remaining()));
            offset += buffer.remaining();
            offset = Mth.roundToward(offset, alignment);
            if (offset >= mapped.slice().length()) {
               break;
            }
         }

         var25 = mapped.slice();
      }

      return var25;
   }

   @Override
   public List<GpuBufferSlice> multiUploadStaging(final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage) {
      return this.multiUpload(data, alignment, usage, true);
   }

   @Override
   public List<GpuBufferSlice> multiUploadGpu(final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage) {
      return this.multiUpload(data, alignment, usage, false);
   }

   public List<GpuBufferSlice> multiUpload(final List<ByteBuffer> data, final long alignment, @GpuBuffer$Usage final int usage, final boolean staging) {
      ReferenceArrayList<GpuBufferSlice> uploadedBuffers = new ReferenceArrayList();
      uploadedBuffers.size(data.size());
      TransientBlockAllocator<?> allocatorInUse = staging ? this.stagingBlockAllocator : this.gpuMappedBlockAllocator;
      IntArrayList sortedDataIndices = IntArrayList.toList(IntStream.range(0, data.size()));
      sortedDataIndices.sort(IntComparator.comparing(index -> data.get(index).remaining()));

      while (!sortedDataIndices.isEmpty()) {
         boolean allocatedAnything = false;

         for (int i = sortedDataIndices.size() - 1; i >= 0; i--) {
            int bufferIndex = sortedDataIndices.getInt(i);
            ByteBuffer currentBuffer = data.get(bufferIndex);
            if (allocatorInUse.canAllocateInCurrentBlock(currentBuffer.remaining(), alignment)) {
               sortedDataIndices.removeInt(i);

               try (GpuBufferSlice$MappedView view = staging
                     ? this.allocateStaging(currentBuffer.remaining(), alignment, usage)
                     : this.allocateGpuMapped(currentBuffer.remaining(), alignment, usage)) {
                  MemoryUtil.memCopy(currentBuffer, view.data());
                  uploadedBuffers.set(bufferIndex, view.slice());
               }

               allocatedAnything = true;
               break;
            }
         }

         if (!allocatedAnything) {
            int bufferIndex = sortedDataIndices.popInt();
            ByteBuffer currentBuffer = data.get(bufferIndex);

            try (GpuBufferSlice$MappedView view = this.allocateGpuMapped(currentBuffer.remaining(), alignment, usage)) {
               MemoryUtil.memCopy(currentBuffer, view.data());
               uploadedBuffers.set(bufferIndex, view.slice());
            }
         }
      }

      return uploadedBuffers;
   }
}
