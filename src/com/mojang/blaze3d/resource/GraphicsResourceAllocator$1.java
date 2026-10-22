package com.mojang.blaze3d.resource;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class GraphicsResourceAllocator$1 implements GraphicsResourceAllocator {
   @Override
   public <T> T acquire(final ResourceDescriptor<T> descriptor) {
      T resource = descriptor.allocate();
      descriptor.prepare(resource);
      return resource;
   }

   @Override
   public <T> void release(final ResourceDescriptor<T> descriptor, final T resource) {
      descriptor.free(resource);
   }
}
