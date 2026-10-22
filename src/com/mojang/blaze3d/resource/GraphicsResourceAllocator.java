package com.mojang.blaze3d.resource;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GraphicsResourceAllocator {
   GraphicsResourceAllocator UNPOOLED = new GraphicsResourceAllocator$1();

   <T> T acquire(ResourceDescriptor<T> descriptor);

   <T> void release(ResourceDescriptor<T> descriptor, T resource);
}
