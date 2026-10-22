package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import com.mojang.renderpearl.api.buffers.TransientMemory;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocation;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocator;
import com.mojang.renderpearl.backend.util.TransientBlockAllocator$Allocator$CpuBlock;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparator;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.List;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vma.Vma;
import org.lwjgl.util.vma.VmaAllocationCreateInfo;
import org.lwjgl.util.vma.VmaAllocationInfo;
import org.lwjgl.vulkan.VK12;
import org.lwjgl.vulkan.VkBufferCopy;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkMemoryHeap;
import org.lwjgl.vulkan.VkMemoryType;
import org.lwjgl.vulkan.VkPhysicalDeviceMemoryProperties;
import org.lwjgl.vulkan.VkBufferCopy.Buffer;

@Environment(EnvType.CLIENT)
public class VulkanTransientMemory implements TransientMemory, Destroyable {
   private static final long BLOCK_SIZE = 524288L;
   private static final long MAX_CPU_ALIGNMENT = 16L;
   private static final long MAX_GPU_ALIGNMENT = Long.highestOneBit(Long.MAX_VALUE);
   private static final int BUFFER_USAGE_BITS = 471;
   private final VulkanDevice device;
   private final VulkanCommandEncoder encoder;
   private final boolean useDeviceMemoryForMappedGpuStaging;
   private final TransientBlockAllocator<TransientBlockAllocator$Allocator$CpuBlock> cpuBlockAllocator = new TransientBlockAllocator<>(
      524288L, 16L, TransientBlockAllocator$Allocator$CpuBlock.memalloc()
   );
   private final TransientBlockAllocator<VulkanTransientMemory$VulkanAllocation> stagingBlockAllocator;
   private final TransientBlockAllocator<VulkanTransientMemory$VulkanAllocation> gpuBlockAllocator;
   private final TransientBlockAllocator<VulkanTransientMemory$TransferPair> gpuMappedBlockAllocator;
   private final int expectedCpuMemoryHeap;
   private final int expectedGpuMemoryHeap;
   private final int[] memoryTypeToHeapMap = new int[32];
   private long submitIndex = 0L;
   private boolean anyCommandRecorded = false;
   @Nullable
   private VkCommandBuffer commandBuffer;

   VulkanTransientMemory(final VulkanDevice device, final VulkanCommandEncoder encoder) {
      this.device = device;
      this.encoder = encoder;
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkDevice vkDevice = device.vkDevice();
         VkPhysicalDeviceMemoryProperties memoryProperties = VkPhysicalDeviceMemoryProperties.calloc(stack);
         VK12.vkGetPhysicalDeviceMemoryProperties(vkDevice.getPhysicalDevice(), memoryProperties);
         int heapCount = memoryProperties.memoryHeapCount();
         int typeCount = memoryProperties.memoryTypeCount();

         for (int i = 0; i < typeCount; i++) {
            this.memoryTypeToHeapMap[i] = memoryProperties.memoryTypes(i).heapIndex();
         }

         int largestDeviceLocalHeapIndex = -1;
         long largestDeviceLocalHeapSize = -1L;

         for (int i = 0; i < heapCount; i++) {
            VkMemoryHeap heapProperties = memoryProperties.memoryHeaps(i);
            if (VulkanUtils.hasAnyBit(heapProperties.flags(), 1) && heapProperties.size() >= largestDeviceLocalHeapSize) {
               largestDeviceLocalHeapIndex = i;
               largestDeviceLocalHeapSize = heapProperties.size();
            }
         }

         assert largestDeviceLocalHeapIndex != -1;

         boolean largestHeapIsHostVisibleAndCoherent = false;

         for (int ix = 0; ix < typeCount; ix++) {
            VkMemoryType typeProperties = memoryProperties.memoryTypes(ix);
            if (typeProperties.heapIndex() == largestDeviceLocalHeapIndex && VulkanUtils.hasAllBits(typeProperties.propertyFlags(), 6)) {
               largestHeapIsHostVisibleAndCoherent = true;
               break;
            }
         }

         this.useDeviceMemoryForMappedGpuStaging = largestHeapIsHostVisibleAndCoherent;
         int selectedCpuHeap = -1;
         if (this.useDeviceMemoryForMappedGpuStaging) {
            selectedCpuHeap = largestDeviceLocalHeapIndex;
         } else {
            for (int ixx = 0; ixx < typeCount; ixx++) {
               VkMemoryType typeProperties = memoryProperties.memoryTypes(ixx);
               if (VulkanUtils.hasAllBits(typeProperties.propertyFlags(), 6)) {
                  VkMemoryHeap heapProperties = memoryProperties.memoryHeaps(typeProperties.heapIndex());
                  if (!VulkanUtils.hasAnyBit(heapProperties.flags(), 1)) {
                     selectedCpuHeap = typeProperties.heapIndex();
                     break;
                  }
               }
            }
         }

         if (selectedCpuHeap == -1) {
            throw new IllegalStateException("Could not select heap for CPU allocations");
         }

         this.expectedCpuMemoryHeap = selectedCpuHeap;
         this.expectedGpuMemoryHeap = largestDeviceLocalHeapIndex;
      } catch (Throwable var17) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var16) {
               var17.addSuppressed(var16);
            }
         }

         throw var17;
      }

      if (stack != null) {
         stack.close();
      }

      this.stagingBlockAllocator = new TransientBlockAllocator<>(
         524288L, MAX_GPU_ALIGNMENT, TransientBlockAllocator$Allocator.create(size -> this.allocateVulkanBlock(size, true), this::freeVulkanBlock)
      );
      this.gpuBlockAllocator = new TransientBlockAllocator<>(
         524288L, MAX_GPU_ALIGNMENT, TransientBlockAllocator$Allocator.create(size -> this.allocateVulkanBlock(size, false), this::queueFreeVulkanBlock)
      );
      this.gpuMappedBlockAllocator = new TransientBlockAllocator<>(
         524288L,
         MAX_GPU_ALIGNMENT,
         TransientBlockAllocator$Allocator.create(this::allocateGpuMappedVulkanBlock, this::freeGpuMappedVulkanBlock),
         this::recordGpuMappedCopy
      );
   }

   @Override
   public void destroy() {
      this.cpuBlockAllocator.close();
      this.stagingBlockAllocator.close();
      this.gpuBlockAllocator.close();
      this.gpuMappedBlockAllocator.close();
   }

   public void beginSubmit() {
      assert this.commandBuffer == null;

      this.commandBuffer = this.encoder.allocateAndBeginTransientCommandBuffer();
      this.encoder.execute(this.commandBuffer);
      this.anyCommandRecorded = false;
   }

   public void endSubmit() {
      this.cpuBlockAllocator.rotate().run();
      this.encoder.queueForDestroy(this.stagingBlockAllocator.rotate()::run);
      if (this.useDeviceMemoryForMappedGpuStaging) {
         this.encoder.queueForDestroy(this.gpuBlockAllocator.rotate()::run);
      } else {
         this.gpuBlockAllocator.rotate().run();
      }

      this.gpuMappedBlockAllocator.rotate();

      assert this.commandBuffer != null;

      if (this.anyCommandRecorded) {
         MemoryStack stack = MemoryStack.stackPush();

         try {
            VulkanCommandEncoder.memoryBarrier(this.commandBuffer, stack);
         } catch (Throwable var5) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (stack != null) {
            stack.close();
         }
      }

      VK12.vkEndCommandBuffer(this.commandBuffer);
      this.commandBuffer = null;
      this.submitIndex++;
   }

   private void recordGpuMappedCopy(final VulkanTransientMemory$TransferPair block) {
      if (block.cpu() != block.gpu()) {
         assert block.cpu().size == block.gpu().size;

         MemoryStack stack = MemoryStack.stackPush();

         try {
            Buffer region = VkBufferCopy.calloc(1, stack);
            region.srcOffset(0L);
            region.dstOffset(0L);
            region.size(block.cpu().size);

            assert this.commandBuffer != null;

            VK12.vkCmdCopyBuffer(this.commandBuffer, block.cpu().vkBuffer, block.gpu().vkBuffer, region);
            this.anyCommandRecorded = true;
         } catch (Throwable var6) {
            if (stack != null) {
               try {
                  stack.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (stack != null) {
            stack.close();
         }
      }
   }

   private VulkanTransientMemory$VulkanAllocation allocateVulkanBlock(final long size, final boolean staging) {
      MemoryStack stack = MemoryStack.stackPush();

      VulkanTransientMemory$VulkanAllocation var14;
      try {
         VkBufferCreateInfo bufferCreateInfo = VkBufferCreateInfo.calloc(stack).sType$Default();
         bufferCreateInfo.size(size);
         bufferCreateInfo.usage(471);
         bufferCreateInfo.sharingMode(0);
         bufferCreateInfo.pQueueFamilyIndices(null);
         VmaAllocationCreateInfo allocCreateInfo = VmaAllocationCreateInfo.calloc(stack);
         int expectedHeap;
         if (staging && !this.useDeviceMemoryForMappedGpuStaging) {
            allocCreateInfo.usage(9);
            expectedHeap = this.expectedCpuMemoryHeap;
         } else {
            allocCreateInfo.usage(8);
            expectedHeap = this.expectedGpuMemoryHeap;
         }

         if (this.useDeviceMemoryForMappedGpuStaging || staging) {
            allocCreateInfo.requiredFlags(6);
            allocCreateInfo.flags(1024);
         }

         LongBuffer bufferPtr = stack.callocLong(1);
         PointerBuffer allocPtr = stack.callocPointer(1);
         VmaAllocationInfo allocationInfo = VmaAllocationInfo.calloc(stack);
         int result = Vma.vmaCreateBuffer(this.device.vma(), bufferCreateInfo, allocCreateInfo, bufferPtr, allocPtr, allocationInfo);
         VulkanUtils.crashIfFailure(this.device, result, "Failed to allocate VkBuffer");
         PointerBuffer hostPtrPtr = stack.callocPointer(1);
         if (staging || this.useDeviceMemoryForMappedGpuStaging) {
            VulkanUtils.crashIfFailure(this.device, Vma.vmaMapMemory(this.device.vma(), allocPtr.get(0), hostPtrPtr), "Failed to map buffer");
         }

         this.device.instance().debug().setObjectName(this.device.vkDevice(), 9, bufferPtr.get(0), "Vulkan Transient Memory Buffer");
         int allocatedHeap = this.memoryTypeToHeapMap[allocationInfo.memoryType()];
         var14 = new VulkanTransientMemory$VulkanAllocation(bufferPtr.get(0), allocPtr.get(0), hostPtrPtr.get(0), size, allocatedHeap != expectedHeap);
      } catch (Throwable var16) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var15) {
               var16.addSuppressed(var15);
            }
         }

         throw var16;
      }

      if (stack != null) {
         stack.close();
      }

      return var14;
   }

   private void queueFreeVulkanBlock(final VulkanTransientMemory$VulkanAllocation allocation) {
      this.encoder.queueForDestroy(() -> this.freeVulkanBlock(allocation));
   }

   private void freeVulkanBlock(final VulkanTransientMemory$VulkanAllocation allocation) {
      Vma.vmaDestroyBuffer(this.device.vma(), allocation.vkBuffer, allocation.vmaAllocation);
   }

   private VulkanTransientMemory$TransferPair allocateGpuMappedVulkanBlock(final long size) {
      assert size >= 524288L;

      assert size >= this.gpuBlockAllocator.blockSize();

      if (this.useDeviceMemoryForMappedGpuStaging) {
         TransientBlockAllocator$Allocation<VulkanTransientMemory$VulkanAllocation> block = this.gpuBlockAllocator.allocate(size, 16L, size, 1L);

         assert block.offset() == 0L;

         return new VulkanTransientMemory$TransferPair(block.block(), block.block());
      } else {
         assert size >= this.stagingBlockAllocator.blockSize();

         TransientBlockAllocator$Allocation<VulkanTransientMemory$VulkanAllocation> stagingBlock = this.stagingBlockAllocator.allocate(size, 16L, size, 1L);
         TransientBlockAllocator$Allocation<VulkanTransientMemory$VulkanAllocation> gpuBlock = this.gpuBlockAllocator.allocate(size, 16L, size, 1L);
         return new VulkanTransientMemory$TransferPair(stagingBlock.block(), gpuBlock.block());
      }
   }

   private void freeGpuMappedVulkanBlock(final VulkanTransientMemory$TransferPair allocations) {
   }

   @Override
   public ByteBuffer allocateCpu(final long size, final long alignment, final long minimumAllocation, final long elementSize) {
      assert size <= 2147483647L;

      TransientBlockAllocator$Allocation<TransientBlockAllocator$Allocator$CpuBlock> alloc = this.cpuBlockAllocator
         .allocate(size, alignment, minimumAllocation, elementSize);
      return MemoryUtil.memByteBuffer(alloc.block().address() + alloc.offset(), (int)alloc.size());
   }

   @Override
   public GpuBufferSlice$MappedView allocateStaging(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      assert size <= 2147483647L;

      TransientBlockAllocator$Allocation<VulkanTransientMemory$VulkanAllocation> alloc = this.stagingBlockAllocator
         .allocate(size, alignment, minimumAllocation, elementSize);
      VulkanTransientMemory$TransientGpuBuffer apiBuffer = new VulkanTransientMemory$TransientGpuBuffer(
         this, alloc.block().vkBuffer, usage, (int)alloc.block().size, this.submitIndex
      );
      ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(alloc.block().hostPtr + alloc.offset(), (int)alloc.size());
      return new GpuBufferSlice$MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {});
   }

   @Override
   public GpuBufferSlice allocateGpu(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      assert size <= 2147483647L;

      TransientBlockAllocator$Allocation<VulkanTransientMemory$VulkanAllocation> alloc = this.gpuBlockAllocator
         .allocate(size, alignment, minimumAllocation, elementSize);
      VulkanTransientMemory$TransientGpuBuffer apiBuffer = new VulkanTransientMemory$TransientGpuBuffer(
         this, alloc.block().vkBuffer, usage, (int)alloc.block().size, this.submitIndex
      );
      return new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size());
   }

   @Override
   public GpuBufferSlice$MappedView allocateGpuMapped(
      final long size, final long alignment, @GpuBuffer$Usage final int usage, final long minimumAllocation, final long elementSize
   ) {
      assert size <= 2147483647L;

      TransientBlockAllocator$Allocation<VulkanTransientMemory$TransferPair> alloc = this.gpuMappedBlockAllocator
         .allocate(size, alignment, minimumAllocation, elementSize);
      VulkanTransientMemory$TransientGpuBuffer apiBuffer = new VulkanTransientMemory$TransientGpuBuffer(
         this, alloc.block().gpu().vkBuffer, usage, (int)alloc.block().gpu().size, this.submitIndex
      );
      ByteBuffer cpuBuffer = MemoryUtil.memByteBuffer(alloc.block().cpu().hostPtr + alloc.offset(), (int)alloc.size());
      return new GpuBufferSlice$MappedView(new GpuBufferSlice(apiBuffer, alloc.offset(), alloc.size()), cpuBuffer, () -> {});
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
