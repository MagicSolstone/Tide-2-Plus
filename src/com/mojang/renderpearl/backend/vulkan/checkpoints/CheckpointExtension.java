package com.mojang.renderpearl.backend.vulkan.checkpoints;

import com.mojang.renderpearl.backend.vulkan.VulkanDevice;
import com.mojang.renderpearl.backend.vulkan.VulkanQueue;
import com.mojang.renderpearl.util.UncheckedAutoCloseable;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface CheckpointExtension extends UncheckedAutoCloseable {
   CheckpointExtension$CheckpointStorage createStorage(VulkanDevice device, VulkanQueue queue, int maxFramesInFlight);

   List<CheckpointExtension$QueueCheckpoints> retrieveCheckpoints(boolean isDeviceLost);
}
