package net.minecraft.client.renderer.rendertype;

import com.google.common.base.Suppliers;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuSampler;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class RenderSetup$RenderSetupBuilder {
   private final RenderPipeline pipeline;
   @Nullable
   private OitPipelineSet oitPipelineSet;
   private boolean useLightmap = false;
   private boolean useOverlay = false;
   private LayeringTransform layeringTransform = LayeringTransform.NO_LAYERING;
   private TextureTransform textureTransform = TextureTransform.DEFAULT_TEXTURING;
   private boolean affectsCrumbling = false;
   private boolean sortOnUpload = false;
   private RenderSetup$OutlineProperty outlineProperty = RenderSetup$OutlineProperty.NONE;
   @Nullable
   private String outlineTextureName;
   private final Map<String, RenderSetup$TextureBinding> textures = new HashMap<>();
   private boolean forceSolidModelPhase;

   private RenderSetup$RenderSetupBuilder(final RenderPipeline pipeline) {
      this.pipeline = pipeline;
   }

   public RenderSetup$RenderSetupBuilder withTexture(final String name, final Identifier texture) {
      this.textures.put(name, new RenderSetup$TextureBinding(texture, () -> null));
      return this;
   }

   public RenderSetup$RenderSetupBuilder withTexture(final String name, final Identifier texture, @Nullable final Supplier<GpuSampler> sampler) {
      this.textures.put(name, new RenderSetup$TextureBinding(texture, Suppliers.memoize(() -> sampler == null ? null : sampler.get())));
      return this;
   }

   public RenderSetup$RenderSetupBuilder useLightmap() {
      this.useLightmap = true;
      return this;
   }

   public RenderSetup$RenderSetupBuilder useOverlay() {
      this.useOverlay = true;
      return this;
   }

   public RenderSetup$RenderSetupBuilder affectsCrumbling() {
      this.affectsCrumbling = true;
      return this;
   }

   public RenderSetup$RenderSetupBuilder sortOnUpload() {
      this.sortOnUpload = true;
      return this;
   }

   public RenderSetup$RenderSetupBuilder setLayeringTransform(final LayeringTransform layeringTransform) {
      this.layeringTransform = layeringTransform;
      return this;
   }

   public RenderSetup$RenderSetupBuilder setTextureTransform(final TextureTransform textureTransform) {
      this.textureTransform = textureTransform;
      return this;
   }

   public RenderSetup$RenderSetupBuilder setOutline(final RenderSetup$OutlineProperty outlineProperty) {
      this.outlineProperty = outlineProperty;
      this.outlineTextureName = null;
      return this;
   }

   public RenderSetup$RenderSetupBuilder setOutline(final RenderSetup$OutlineProperty outlineProperty, final String outlineTextureName) {
      this.outlineProperty = outlineProperty;
      this.outlineTextureName = outlineTextureName;
      return this;
   }

   public RenderSetup$RenderSetupBuilder setOitPipelines(final OitPipelineSet oitPipelineSet) {
      this.oitPipelineSet = oitPipelineSet;
      return this;
   }

   public RenderSetup$RenderSetupBuilder withForcedSolidModelPhase() {
      this.forceSolidModelPhase = true;
      return this;
   }

   public RenderSetup createRenderSetup() {
      return new RenderSetup(
         this.pipeline,
         this.oitPipelineSet,
         this.textures,
         this.useLightmap,
         this.useOverlay,
         this.layeringTransform,
         this.textureTransform,
         this.outlineProperty,
         this.outlineTextureName,
         this.affectsCrumbling,
         this.sortOnUpload,
         this.forceSolidModelPhase
      );
   }
}
