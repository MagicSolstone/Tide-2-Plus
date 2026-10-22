package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.commands.GpuFence;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class VulkanCommandEncoder$1 implements GpuFence {
   private final long submitIndex;
   private boolean completed;

   VulkanCommandEncoder$1(final VulkanCommandEncoder this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.submitIndex = this.this$0.currentSubmitIndex;
      this.completed = false;
   }

   @Override
   public boolean awaitCompletion(final long timeoutMs) {
      if (!this.completed) {
         this.completed = this.this$0.awaitSubmitCompletion(this.submitIndex, timeoutMs);
      }

      return this.completed;
   }

   @Override
   public void close() {
      this.completed = true;
   }
}
