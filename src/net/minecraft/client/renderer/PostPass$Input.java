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
public interface PostPass$Input {
   void addToPass(FramePass pass, Map<Identifier, ResourceHandle<RenderTarget>> targets);

   default void cleanup(final Map<Identifier, ResourceHandle<RenderTarget>> targets) {
   }

   GpuTextureView texture(final Map<Identifier, ResourceHandle<RenderTarget>> targets);

   String samplerName();

   boolean bilinear();
}
