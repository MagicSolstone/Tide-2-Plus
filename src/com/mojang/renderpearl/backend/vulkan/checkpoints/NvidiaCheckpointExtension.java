package com.mojang.renderpearl.backend.vulkan.checkpoints;

import com.mojang.renderpearl.backend.vulkan.VulkanDevice;
import com.mojang.renderpearl.backend.vulkan.VulkanQueue;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NvidiaCheckpointExtension implements CheckpointExtension {
   private final List<NvidiaCheckpointExtension$NvidiaCheckpointStorage> storages = new ArrayList<>();

   @Override
   public CheckpointExtension$CheckpointStorage createStorage(final VulkanDevice device, final VulkanQueue queue, final int maxFramesInFlight) {
      NvidiaCheckpointExtension$NvidiaCheckpointStorage storage = new NvidiaCheckpointExtension$NvidiaCheckpointStorage(queue, maxFramesInFlight);
      this.storages.add(storage);
      return storage;
   }

   @Override
   public List<CheckpointExtension$QueueCheckpoints> retrieveCheckpoints(final boolean isDeviceLost) {
      if (!isDeviceLost) {
         return List.of();
      } else {
         List<CheckpointExtension$QueueCheckpoints> result = new ArrayList<>(this.storages.size());

         for (NvidiaCheckpointExtension$NvidiaCheckpointStorage storage : this.storages) {
            result.add(storage.retrieveCheckpoints());
         }

         return result;
      }
   }

   @Override
   public void close() {
   }
}
