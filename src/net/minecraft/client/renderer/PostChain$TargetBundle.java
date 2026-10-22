package net.minecraft.client.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface PostChain$TargetBundle {
   static PostChain$TargetBundle of(final Identifier targetId, final ResourceHandle<RenderTarget> target) {
      return new PostChain$TargetBundle$1(target, targetId);
   }

   void replace(Identifier id, ResourceHandle<RenderTarget> handle);

   @Nullable
   ResourceHandle<RenderTarget> get(Identifier id);

   default ResourceHandle<RenderTarget> getOrThrow(final Identifier id) {
      ResourceHandle<RenderTarget> handle = this.get(id);
      if (handle == null) {
         throw new IllegalArgumentException("Missing target with id " + id);
      } else {
         return handle;
      }
   }
}
