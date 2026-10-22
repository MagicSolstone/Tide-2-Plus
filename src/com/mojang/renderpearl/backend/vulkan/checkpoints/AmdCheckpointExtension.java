package com.mojang.renderpearl.backend.vulkan.checkpoints;

import com.mojang.renderpearl.backend.vulkan.VulkanDevice;
import com.mojang.renderpearl.backend.vulkan.VulkanQueue;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AmdCheckpointExtension implements CheckpointExtension {
   private static final long[] STAGES = new long[]{1L, 8192L};
   private final List<AmdCheckpointExtension$AmdCheckpointStorage> storages = new ArrayList<>();

   @Override
   public CheckpointExtension$CheckpointStorage createStorage(final VulkanDevice device, final VulkanQueue queue, final int maxFramesInFlight) {
      AmdCheckpointExtension$AmdCheckpointStorage storage = new AmdCheckpointExtension$AmdCheckpointStorage(device, queue, maxFramesInFlight);
      this.storages.add(storage);
      return storage;
   }

   @Override
   public List<CheckpointExtension$QueueCheckpoints> retrieveCheckpoints(final boolean isDeviceLost) {
      List<CheckpointExtension$QueueCheckpoints> result = new ArrayList<>(this.storages.size());

      for (AmdCheckpointExtension$AmdCheckpointStorage storage : this.storages) {
         result.add(storage.retrieveCheckpoints());
      }

      return result;
   }

   @Override
   public void close() {
      for (AmdCheckpointExtension$AmdCheckpointStorage storage : this.storages) {
         storage.close();
      }
   }
}
