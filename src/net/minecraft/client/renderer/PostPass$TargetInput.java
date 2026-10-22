package net.minecraft.client.renderer;

import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record PostPass$TargetInput(String samplerName, Identifier targetId, boolean depthBuffer, boolean bilinear) implements PostPass$Input {
   private ResourceHandle<RenderTarget> getHandle(final Map<Identifier, ResourceHandle<RenderTarget>> targets) {
      ResourceHandle<RenderTarget> handle = targets.get(this.targetId);
      if (handle == null) {
         throw new IllegalStateException("Missing handle for target " + this.targetId);
      } else {
         return handle;
      }
   }

   @Override
   public void addToPass(final FramePass pass, final Map<Identifier, ResourceHandle<RenderTarget>> targets) {
      pass.reads(this.getHandle(targets));
   }

   @Override
   public GpuTextureView texture(final Map<Identifier, ResourceHandle<RenderTarget>> targets) {
      ResourceHandle<RenderTarget> handle = this.getHandle(targets);
      RenderTarget target = handle.get();
      GpuTextureView textureView = this.depthBuffer ? target.getDepthTextureView() : target.getColorTextureView();
      if (textureView == null) {
         throw new IllegalStateException("Missing " + (this.depthBuffer ? "depth" : "color") + "texture for target " + this.targetId);
      } else {
         return textureView;
      }
   }
}
