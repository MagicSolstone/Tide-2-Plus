package net.minecraft.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.device.GpuDevice;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class DynamicGpuDataStorageNonMapped<T extends DynamicGpuDataStorage$DynamicGpuData> implements DynamicGpuDataStorage<T> {
   private static final Logger LOGGER = LogUtils.getLogger();
   private final List<GpuBuffer> oldBuffers = new ArrayList<>();
   private final int blockSize;
   private GpuBuffer gpuBuffer;
   private ByteBuffer cpuBuffer;
   private int nextBlock;
   private int capacity;
   private final String label;
   @GpuBuffer$Usage
   private final int usage;

   public DynamicGpuDataStorageNonMapped(final String label, final int dataSize, @GpuBuffer$Usage final int usage, final int initialCapacity) {
      GpuDevice device = RenderSystem.getDevice();
      this.usage = usage;
      this.blockSize = usage == 128 ? Mth.roundToward(dataSize, device.getDeviceInfo().limits().minUniformOffsetAlignment()) : dataSize;
      this.capacity = Mth.smallestEncompassingPowerOfTwo(initialCapacity);
      this.nextBlock = 0;
      this.gpuBuffer = RenderSystem.getDevice().createBuffer(() -> label + " x" + this.blockSize, this.usage | 8, this.blockSize * this.capacity);
      this.cpuBuffer = MemoryUtil.memAlloc(this.blockSize * this.capacity);
      this.label = label;
   }

   @Override
   public void endFrame() {
      this.nextBlock = 0;
      this.cpuBuffer.clear();

      for (GpuBuffer oldBuffer : this.oldBuffers) {
         oldBuffer.close();
      }

      this.oldBuffers.clear();
   }

   @Override
   public GpuBufferSlice writeData(final T gpuData) {
      throw new NotImplementedException("writeData");
   }

   @Override
   public GpuBufferSlice[] writeData(final T[] dataArray) {
      if (dataArray.length == 0) {
         return new GpuBufferSlice[0];
      } else {
         if (this.nextBlock + dataArray.length > this.capacity) {
            int newCapacity = Mth.smallestEncompassingPowerOfTwo(Math.max(this.capacity + 1, dataArray.length));
            LOGGER.info(
               "Resizing {}, capacity limit of {} reached during a single frame. New capacity will be {}.",
               new Object[]{this.label, this.capacity, newCapacity}
            );
            this.resizeBuffers(newCapacity);
         }

         GpuBufferSlice[] result = new GpuBufferSlice[dataArray.length];
         int firstOffset = this.nextBlock * this.blockSize;

         for (int i = 0; i < dataArray.length; i++) {
            T data = dataArray[i];
            result[i] = this.gpuBuffer.slice(firstOffset + i * this.blockSize, this.blockSize);
            this.cpuBuffer.position(firstOffset + i * this.blockSize);
            data.write(this.cpuBuffer);
         }

         ByteBuffer cpuSlice = this.cpuBuffer.slice(firstOffset, dataArray.length * this.blockSize);
         GpuBufferSlice writeSlice = this.gpuBuffer.slice(firstOffset, dataArray.length * this.blockSize);
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(writeSlice, cpuSlice);
         this.nextBlock += dataArray.length;
         return result;
      }
   }

   private void resizeBuffers(final int newCapacity) {
      this.capacity = newCapacity;
      this.nextBlock = 0;
      this.oldBuffers.add(this.gpuBuffer);
      this.gpuBuffer = RenderSystem.getDevice().createBuffer(() -> this.label + " x" + this.blockSize, this.usage | 8, this.blockSize * this.capacity);
      this.cpuBuffer = MemoryUtil.memRealloc(this.cpuBuffer, this.blockSize * this.capacity);
   }

   @Override
   public GpuBufferSlice writeDataBatched(final List<T> dataList) {
      if (dataList.isEmpty()) {
         return new GpuBufferSlice(null, 0L, 0L);
      } else {
         if (this.nextBlock + dataList.size() > this.capacity) {
            int newCapacity = Mth.smallestEncompassingPowerOfTwo(Math.max(this.capacity + 1, dataList.size()));
            LOGGER.info(
               "Resizing {}, capacity limit of {} reached during a single frame. New capacity will be {}.",
               new Object[]{this.label, this.capacity, newCapacity}
            );
            this.resizeBuffers(newCapacity);
         }

         int firstOffset = this.nextBlock * this.blockSize;

         for (int i = 0; i < dataList.size(); i++) {
            T data = dataList.get(i);
            this.cpuBuffer.position(firstOffset + i * this.blockSize);
            data.write(this.cpuBuffer);
         }

         ByteBuffer cpuSlice = this.cpuBuffer.slice(firstOffset, dataList.size() * this.blockSize);
         GpuBufferSlice result = this.gpuBuffer.slice(firstOffset, dataList.size() * this.blockSize);
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(result, cpuSlice);
         this.nextBlock = this.nextBlock + dataList.size();
         return result;
      }
   }

   @Override
   public GpuBufferSlice[] writeDataBatchedMultiple(final List<List<T>> dataLists) {
      if (dataLists.isEmpty()) {
         return new GpuBufferSlice[0];
      } else {
         int totalCount = 0;

         for (List<T> data : dataLists) {
            totalCount += data.size();
         }

         if (this.nextBlock + totalCount > this.capacity) {
            int newCapacity = Mth.smallestEncompassingPowerOfTwo(Math.max(this.capacity + 1, totalCount));
            LOGGER.info(
               "Resizing {}, capacity limit of {} reached during a single frame. New capacity will be {}.",
               new Object[]{this.label, this.capacity, newCapacity}
            );
            this.resizeBuffers(newCapacity);
         }

         int firstOffset = this.nextBlock * this.blockSize;
         int offset = firstOffset;
         GpuBufferSlice[] result = new GpuBufferSlice[dataLists.size()];
         int bufferPositionIndex = 0;

         for (int i = 0; i < dataLists.size(); i++) {
            List<T> dataList = dataLists.get(i);
            result[i] = this.gpuBuffer.slice(offset, dataList.size() * this.blockSize);

            for (T data : dataList) {
               this.cpuBuffer.position(firstOffset + bufferPositionIndex++ * this.blockSize);
               data.write(this.cpuBuffer);
            }

            offset += dataList.size() * this.blockSize;
         }

         ByteBuffer cpuSlice = this.cpuBuffer.slice(firstOffset, totalCount * this.blockSize);
         GpuBufferSlice writeSlice = this.gpuBuffer.slice(firstOffset, totalCount * this.blockSize);
         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(writeSlice, cpuSlice);
         this.nextBlock += totalCount;
         return result;
      }
   }

   @GpuBuffer$Usage
   @Override
   public int usage() {
      return this.usage;
   }

   @Override
   public void close() {
      for (GpuBuffer oldBuffer : this.oldBuffers) {
         oldBuffer.close();
      }

      this.oldBuffers.clear();
      this.gpuBuffer.close();
      MemoryUtil.memFree(this.cpuBuffer);
   }
}
