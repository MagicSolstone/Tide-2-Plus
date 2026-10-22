package net.minecraft.client.particle;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.oit.OitPipelineSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record SingleQuadParticle$Layer(boolean translucent, Identifier textureAtlasLocation, RenderPipeline pipeline, @Nullable OitPipelineSet oitPipelineSet) {
   public static final SingleQuadParticle$Layer OPAQUE_TERRAIN = new SingleQuadParticle$Layer(
      false, TextureAtlas.LOCATION_BLOCKS, RenderPipelines.OPAQUE_PARTICLE
   );
   public static final SingleQuadParticle$Layer TRANSLUCENT_TERRAIN = new SingleQuadParticle$Layer(
      true, TextureAtlas.LOCATION_BLOCKS, RenderPipelines.TRANSLUCENT_PARTICLE, RenderPipelines.OIT_PARTICLE
   );
   public static final SingleQuadParticle$Layer OPAQUE_ITEMS = new SingleQuadParticle$Layer(false, TextureAtlas.LOCATION_ITEMS, RenderPipelines.OPAQUE_PARTICLE);
   public static final SingleQuadParticle$Layer TRANSLUCENT_ITEMS = new SingleQuadParticle$Layer(
      true, TextureAtlas.LOCATION_ITEMS, RenderPipelines.TRANSLUCENT_PARTICLE, RenderPipelines.OIT_PARTICLE
   );
   public static final SingleQuadParticle$Layer OPAQUE = new SingleQuadParticle$Layer(false, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.OPAQUE_PARTICLE);
   public static final SingleQuadParticle$Layer TRANSLUCENT = new SingleQuadParticle$Layer(
      true, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.TRANSLUCENT_PARTICLE, RenderPipelines.OIT_PARTICLE
   );

   public SingleQuadParticle$Layer(final boolean translucent, final Identifier textureAtlasLocation, final RenderPipeline pipeline) {
      this(translucent, textureAtlasLocation, pipeline, null);
   }

   public static SingleQuadParticle$Layer bySprite(final TextureAtlasSprite sprite) {
      boolean translucent = sprite.transparency().hasTranslucent();
      if (sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS)) {
         return translucent ? TRANSLUCENT_TERRAIN : OPAQUE_TERRAIN;
      } else if (sprite.atlasLocation().equals(TextureAtlas.LOCATION_ITEMS)) {
         return translucent ? TRANSLUCENT_ITEMS : OPAQUE_ITEMS;
      } else {
         return translucent ? TRANSLUCENT : OPAQUE;
      }
   }
}
