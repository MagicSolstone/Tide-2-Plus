package com.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
record UberGpuBuffer$StagedAllocationEntry<T>(StagingBuffer$BufferHandle buffer, @Nullable UberGpuBuffer$UploadCallback<T> callback) implements AutoCloseable {
   @Override
   public void close() {
      this.buffer.close();
   }
}
