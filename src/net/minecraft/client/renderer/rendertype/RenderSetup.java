package net.minecraft.client.renderer.rendertype;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.blaze3d.systems.SamplerCache;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class RenderSetup {
   final RenderPipeline pipeline;
   @Nullable
   final OitPipelineSet oitPipelineSet;
   final Map<String, RenderSetup$TextureBinding> textures;
   final TextureTransform textureTransform;
   final RenderSetup$OutlineProperty outlineProperty;
   @Nullable
   final String outlineTextureName;
   final boolean useLightmap;
   final boolean useOverlay;
   final boolean affectsCrumbling;
   final boolean sortOnUpload;
   final LayeringTransform layeringTransform;
   final boolean forceSolidModelPhase;

   private RenderSetup(
      final RenderPipeline pipeline,
      @Nullable final OitPipelineSet oitPipelineSet,
      final Map<String, RenderSetup$TextureBinding> textures,
      final boolean useLightmap,
      final boolean useOverlay,
      final LayeringTransform layeringTransform,
      final TextureTransform textureTransform,
      final RenderSetup$OutlineProperty outlineProperty,
      @Nullable final String outlineTextureName,
      final boolean affectsCrumbling,
      final boolean sortOnUpload,
      final boolean forceSolidModelPhase
   ) {
      this.pipeline = pipeline;
      this.oitPipelineSet = oitPipelineSet;
      this.textures = textures;
      this.textureTransform = textureTransform;
      this.useLightmap = useLightmap;
      this.useOverlay = useOverlay;
      this.outlineProperty = outlineProperty;
      this.outlineTextureName = outlineTextureName;
      this.layeringTransform = layeringTransform;
      this.affectsCrumbling = affectsCrumbling;
      this.sortOnUpload = sortOnUpload;
      this.forceSolidModelPhase = forceSolidModelPhase;
   }

   @Override
   public String toString() {
      return "RenderSetup[layeringTransform="
         + this.layeringTransform
         + ", textureTransform="
         + this.textureTransform
         + ", textures="
         + this.textures
         + ", outlineProperty="
         + this.outlineProperty
         + ", useLightmap="
         + this.useLightmap
         + ", useOverlay="
         + this.useOverlay
         + "]";
   }

   public static RenderSetup$RenderSetupBuilder builder(final RenderPipeline pipeline) {
      return new RenderSetup$RenderSetupBuilder(pipeline);
   }

   public List<PreparedRenderType$Texture> prepareTextures(
      final TextureManager textureManager, final SamplerCache samplerCache, final GpuTextureView overlayTexture, final GpuTextureView lightmapTexture
   ) {
      if (this.textures.isEmpty() && !this.useOverlay && !this.useLightmap) {
         return List.of();
      } else {
         Builder<PreparedRenderType$Texture> textures = ImmutableList.builderWithExpectedSize(this.textures.size() + 2);
         if (this.useOverlay) {
            textures.add(new PreparedRenderType$Texture("Sampler1", overlayTexture, samplerCache.getClampToEdge(FilterMode.LINEAR)));
         }

         if (this.useLightmap) {
            textures.add(new PreparedRenderType$Texture("Sampler2", lightmapTexture, samplerCache.getClampToEdge(FilterMode.LINEAR)));
         }

         for (Entry<String, RenderSetup$TextureBinding> entry : this.textures.entrySet()) {
            AbstractTexture texture = textureManager.getTexture(entry.getValue().location);
            GpuSampler samplerOverride = entry.getValue().sampler().get();
            textures.add(
               new PreparedRenderType$Texture(entry.getKey(), texture.getTextureView(), samplerOverride != null ? samplerOverride : texture.getSampler())
            );
         }

         return textures.build();
      }
   }
}
