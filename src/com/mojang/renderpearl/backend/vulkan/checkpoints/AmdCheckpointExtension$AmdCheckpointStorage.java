package com.mojang.renderpearl.backend.vulkan.checkpoints;

import com.mojang.renderpearl.api.buffers.GpuBufferSlice$MappedView;
import com.mojang.renderpearl.backend.vulkan.VulkanDevice;
import com.mojang.renderpearl.backend.vulkan.VulkanGpuBuffer;
import com.mojang.renderpearl.backend.vulkan.VulkanQueue;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.vulkan.AMDBufferMarker;
import org.lwjgl.vulkan.VkCommandBuffer;

@Environment(EnvType.CLIENT)
class AmdCheckpointExtension$AmdCheckpointStorage extends AbstractCheckpointStorage implements UncheckedAutoCloseable {
   private final VulkanGpuBuffer buffer;
   private final GpuBufferSlice$MappedView mappedView;

   protected AmdCheckpointExtension$AmdCheckpointStorage(final VulkanDevice device, final VulkanQueue queue, final int maxFramesInFlight) {
      super(queue, maxFramesInFlight);
      this.buffer = device.createBuffer(() -> "Internal marker storage", 9, (long)(AmdCheckpointExtension.STAGES.length * 4));
      this.mappedView = this.buffer.map(true, false);
   }

   @Override
   protected void recordCheckpoint(final VkCommandBuffer commandBuffer, final int id) {
      for (int i = 0; i < AmdCheckpointExtension.STAGES.length; i++) {
         AMDBufferMarker.vkCmdWriteBufferMarker2AMD(commandBuffer, AmdCheckpointExtension.STAGES[i], this.buffer.vkBuffer(), i * 4, id);
      }
   }

   public CheckpointExtension$QueueCheckpoints retrieveCheckpoints() {
      List<CheckpointExtension$StageCheckpoint> stageCheckpoints = new ArrayList<>();

      for (int i = 0; i < AmdCheckpointExtension.STAGES.length; i++) {
         AbstractCheckpointStorage$Checkpoint checkpoint = this.findCheckpoint(this.mappedView.data().getInt(i * 4));
         if (checkpoint != null) {
            stageCheckpoints.add(new CheckpointExtension$StageCheckpoint(AmdCheckpointExtension.STAGES[i], checkpoint.type(), checkpoint.label()));
         }
      }

      return new CheckpointExtension$QueueCheckpoints(this.queue.address(), stageCheckpoints);
   }

   @Override
   public void close() {
      this.mappedView.close();
      this.buffer.close();
   }
}
