package com.mojang.blaze3d.framegraph;

import com.mojang.blaze3d.resource.ResourceDescriptor;
import com.mojang.blaze3d.resource.ResourceHandle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface FramePass {
   <T> ResourceHandle<T> createsInternal(String name, ResourceDescriptor<T> descriptor);

   <T> void reads(ResourceHandle<T> handle);

   <T> ResourceHandle<T> readsAndWrites(ResourceHandle<T> handle);

   void requires(FramePass pass);

   void disableCulling();

   void executes(Runnable task);
}
