package net.minecraft.client.renderer;

import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record PostPass$TextureInput(String samplerName, AbstractTexture texture, int width, int height, boolean bilinear) implements PostPass$Input {
   @Override
   public void addToPass(final FramePass pass, final Map<Identifier, ResourceHandle<RenderTarget>> targets) {
   }

   @Override
   public GpuTextureView texture(final Map<Identifier, ResourceHandle<RenderTarget>> targets) {
      return this.texture.getTextureView();
   }
}
