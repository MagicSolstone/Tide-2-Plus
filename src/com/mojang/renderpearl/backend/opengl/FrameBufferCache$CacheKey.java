package com.mojang.renderpearl.backend.opengl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FrameBufferCache$CacheKey {
   private final int[] data;
   private final int hash;
   public final List<FrameBufferAttachment> associatedAttachments;

   public FrameBufferCache$CacheKey(
      final List<FrameBufferAttachment> colorAttachments, @Nullable final FrameBufferAttachment depthAttachment, final int mipOffset
   ) {
      int colorAttachmentCount = colorAttachments.size();
      this.data = new int[(colorAttachmentCount + (depthAttachment != null ? 1 : 0)) * 2];

      for (int i = 0; i < colorAttachmentCount; i++) {
         FrameBufferAttachment attachment = colorAttachments.get(i);
         if (attachment != null) {
            this.data[i * 2] = attachment.glId();
            this.data[i * 2 + 1] = attachment.fboMipLevel() + mipOffset;
         } else {
            this.data[i * 2] = 0;
            this.data[i * 2 + 1] = 0;
         }
      }

      this.associatedAttachments = new ArrayList<>(colorAttachments);
      if (depthAttachment != null) {
         this.data[colorAttachmentCount * 2] = depthAttachment.glId();
         this.data[colorAttachmentCount * 2 + 1] = depthAttachment.fboMipLevel() + mipOffset;
         this.associatedAttachments.add(depthAttachment);
      }

      this.hash = Arrays.hashCode(this.data);
   }

   @Override
   public int hashCode() {
      return this.hash;
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      } else if (obj instanceof FrameBufferCache$CacheKey other) {
         return this.hash != other.hash ? false : Arrays.equals(this.data, other.data);
      } else {
         return false;
      }
   }
}
