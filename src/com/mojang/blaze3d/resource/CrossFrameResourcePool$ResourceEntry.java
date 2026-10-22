package com.mojang.blaze3d.resource;

import com.google.common.annotations.VisibleForTesting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@VisibleForTesting
public final class CrossFrameResourcePool$ResourceEntry<T> implements AutoCloseable {
   private final ResourceDescriptor<T> descriptor;
   private final T value;
   private int framesToLive;

   private CrossFrameResourcePool$ResourceEntry(final ResourceDescriptor<T> descriptor, final T value, final int framesToLive) {
      this.descriptor = descriptor;
      this.value = value;
      this.framesToLive = framesToLive;
   }

   @Override
   public void close() {
      this.descriptor.free(this.value);
   }
}
