package net.minecraft.client.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class PostChain$TargetBundle$1 implements PostChain$TargetBundle {
   private ResourceHandle<RenderTarget> handle;

   PostChain$TargetBundle$1(final ResourceHandle val$target, final Identifier val$targetId) {
      this.val$target = val$targetx;
      this.val$targetId = val$targetIdx;
      this.handle = this.val$target;
   }

   @Override
   public void replace(final Identifier id, final ResourceHandle<RenderTarget> handle) {
      if (id.equals(this.val$targetId)) {
         this.handle = handle;
      } else {
         throw new IllegalArgumentException("No target with id " + id);
      }
   }

   @Nullable
   @Override
   public ResourceHandle<RenderTarget> get(final Identifier id) {
      return id.equals(this.val$targetId) ? this.handle : null;
   }
}
