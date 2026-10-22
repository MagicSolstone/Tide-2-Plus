package com.mojang.renderpearl.backend.vulkan.checkpoints;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.vulkan.VkCommandBuffer;

@Environment(EnvType.CLIENT)
public interface CheckpointExtension$CheckpointStorage {
   void rotate();

   void recordCheckpoint(VkCommandBuffer commandBuffer, CheckpointExtension$CheckpointType type, Supplier<String> label);
}
