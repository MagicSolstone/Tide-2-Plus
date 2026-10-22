package com.mojang.renderpearl.backend.opengl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface FrameBufferAttachment {
   int glId();

   int fboMipLevel();

   void addAssociatedFbo(FrameBufferCache$CacheKey fboKey);

   void removeAssociatedFbo(FrameBufferCache$CacheKey fboKey);
}
