package com.mojang.renderpearl.backend.opengl;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FrameBufferCache {
   private final Object2IntMap<FrameBufferCache$CacheKey> cache = new Object2IntOpenHashMap();

   public int getFbo(final DirectStateAccess dsa, final List<FrameBufferAttachment> colorTextures, @Nullable final FrameBufferAttachment depthTexture) {
      return this.getFbo(dsa, colorTextures, depthTexture, 0);
   }

   public int getFbo(
      final DirectStateAccess dsa, final List<FrameBufferAttachment> colorTextures, @Nullable final FrameBufferAttachment depthTexture, final int mipOffset
   ) {
      FrameBufferCache$CacheKey cacheKey = new FrameBufferCache$CacheKey(colorTextures, depthTexture, mipOffset);
      return this.cache.computeIfAbsent(cacheKey, var6 -> this.createFbo(cacheKey, dsa, colorTextures, depthTexture, mipOffset));
   }

   private int createFbo(
      final FrameBufferCache$CacheKey key,
      final DirectStateAccess dsa,
      final List<FrameBufferAttachment> colorAttachments,
      @Nullable final FrameBufferAttachment depthAttachment,
      final int mipOffset
   ) {
      int fbo = dsa.createFrameBufferObject();
      int colorAttachmentCount = colorAttachments.size();
      int[] colorIds = new int[colorAttachmentCount];
      int[] mipLevels = new int[colorAttachmentCount];

      for (int i = 0; i < colorAttachmentCount; i++) {
         FrameBufferAttachment attachment = colorAttachments.get(i);
         if (attachment != null) {
            colorIds[i] = attachment.glId();
            mipLevels[i] = attachment.fboMipLevel() + mipOffset;
            attachment.addAssociatedFbo(key);
         } else {
            colorIds[i] = 0;
            mipLevels[i] = 0;
         }
      }

      if (depthAttachment != null) {
         depthAttachment.addAssociatedFbo(key);
      }

      dsa.bindFrameBufferTextures(
         fbo, colorIds, mipLevels, depthAttachment == null ? 0 : depthAttachment.glId(), depthAttachment == null ? 0 : depthAttachment.fboMipLevel(), 0
      );
      return fbo;
   }

   public void destroyFbo(final FrameBufferCache$CacheKey key) {
      if (this.cache.containsKey(key)) {
         for (FrameBufferAttachment associatedAttachment : key.associatedAttachments) {
            if (associatedAttachment != null) {
               associatedAttachment.removeAssociatedFbo(key);
            }
         }

         int fboId = this.cache.removeInt(key);
         GlStateManager._glDeleteFramebuffers(fboId);
      }
   }
}
