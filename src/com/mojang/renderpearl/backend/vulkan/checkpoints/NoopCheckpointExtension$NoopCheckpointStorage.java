package com.mojang.renderpearl.backend.vulkan.checkpoints;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.vulkan.VkCommandBuffer;

@Environment(EnvType.CLIENT)
class NoopCheckpointExtension$NoopCheckpointStorage implements CheckpointExtension$CheckpointStorage {
   private static final NoopCheckpointExtension$NoopCheckpointStorage INSTANCE = new NoopCheckpointExtension$NoopCheckpointStorage();

   private NoopCheckpointExtension$NoopCheckpointStorage() {
   }

   @Override
   public void rotate() {
   }

   @Override
   public void recordCheckpoint(final VkCommandBuffer commandBuffer, final CheckpointExtension$CheckpointType type, final Supplier<String> label) {
   }
}
