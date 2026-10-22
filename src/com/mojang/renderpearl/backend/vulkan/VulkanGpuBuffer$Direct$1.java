package com.mojang.renderpearl.backend.vulkan;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.util.vma.Vma;

@Environment(EnvType.CLIENT)
class VulkanGpuBuffer$Direct$1 implements Runnable {
   private boolean closed;

   VulkanGpuBuffer$Direct$1(final VulkanGpuBuffer$Direct this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.closed = false;
   }

   @Override
   public void run() {
      if (!this.closed) {
         this.closed = true;
         this.this$0.mappingRefCount--;
         Vma.vmaUnmapMemory(this.this$0.device.vma(), this.this$0.vmaAllocation);
      }
   }
}
