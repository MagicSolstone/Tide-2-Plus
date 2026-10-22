package net.minecraft.client.renderer;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.commands.GpuFence;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class StagedVertexBuffer$GpuBufferPool implements AutoCloseable {
   private static final int BUFFER_SIZE_INCREMENT = 262144;
   private static final int MAX_REUSE_SIZE_FACTOR = 4;
   private final Supplier<String> label;
   @GpuBuffer$Usage
   private final int usage;
   private final List<GpuBuffer> available = new ArrayList<>();
   private final List<GpuBuffer> usedThisFrame = new ArrayList<>();
   private final List<StagedVertexBuffer$GpuBufferPool$PendingRecycle> pendingRecycle = new ArrayList<>();

   private StagedVertexBuffer$GpuBufferPool(final Supplier<String> label, @GpuBuffer$Usage final int usage) {
      this.label = label;
      this.usage = usage;
   }

   private void tryRecycleBuffers() {
      this.pendingRecycle.removeIf(buffer -> {
         List<GpuBuffer> recycled = buffer.tryRecycle();
         if (recycled != null) {
            this.available.addAll(recycled);
            return true;
         } else {
            return false;
         }
      });
   }

   public GpuBuffer acquire(final GpuDevice device, final int minSize) {
      int roundedMinSize = Mth.roundToward(minSize, 262144);
      GpuBuffer buffer = this.takeBestAvailable(roundedMinSize, roundedMinSize * 4);
      if (buffer == null) {
         buffer = device.createBuffer(this.label, this.usage, roundedMinSize);
      }

      this.usedThisFrame.add(buffer);
      return buffer;
   }

   @Nullable
   private GpuBuffer takeBestAvailable(final int minSize, final int maxSize) {
      int bestIndex = -1;
      long bestSize = maxSize + 1;

      for (int i = 0; i < this.available.size(); i++) {
         long size = this.available.get(i).size();
         if (size == minSize) {
            return this.available.remove(i);
         }

         if (size > minSize && size < bestSize) {
            bestIndex = i;
            bestSize = size;
         }
      }

      return bestIndex == -1 ? null : this.available.remove(bestIndex);
   }

   public void endFrame(final GpuDevice device) {
      if (!this.usedThisFrame.isEmpty()) {
         GpuFence fence = device.createCommandEncoder().createFence();
         this.pendingRecycle.add(new StagedVertexBuffer$GpuBufferPool$PendingRecycle(List.copyOf(this.usedThisFrame), fence));
         this.usedThisFrame.clear();
      }

      if (!this.available.isEmpty()) {
         this.available.forEach(UncheckedAutoCloseable::close);
         this.available.clear();
      }

      this.tryRecycleBuffers();
   }

   @Override
   public void close() {
      this.available.forEach(UncheckedAutoCloseable::close);
      this.usedThisFrame.forEach(UncheckedAutoCloseable::close);
      this.pendingRecycle.forEach(StagedVertexBuffer$GpuBufferPool$PendingRecycle::close);
      this.available.clear();
      this.usedThisFrame.clear();
      this.pendingRecycle.clear();
   }
}
