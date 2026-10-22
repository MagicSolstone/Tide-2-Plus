package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.GlyphInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class AtlasGlyphProvider {
   private static final GlyphInfo GLYPH_INFO = GlyphInfo.simple(8.0F);
   private final TextureAtlas atlas;
   private final GlyphRenderTypes renderTypes;
   private final GlyphSource missingWrapper;
   private final Map<Identifier, GlyphSource> wrapperCache = new HashMap<>();
   private final Function<Identifier, GlyphSource> spriteResolver;

   public AtlasGlyphProvider(final TextureAtlas atlas) {
      this.atlas = atlas;
      this.renderTypes = GlyphRenderTypes.createForColorTexture(atlas.location());
      TextureAtlasSprite missingSprite = atlas.missingSprite();
      this.missingWrapper = this.createSprite(missingSprite);
      this.spriteResolver = id -> {
         TextureAtlasSprite sprite = atlas.getSprite(id);
         return sprite == missingSprite ? this.missingWrapper : this.createSprite(sprite);
      };
   }

   public GlyphSource sourceForSprite(final Identifier spriteId) {
      return this.wrapperCache.computeIfAbsent(spriteId, this.spriteResolver);
   }

   private GlyphSource createSprite(final TextureAtlasSprite sprite) {
      return new SingleSpriteSource(new AtlasGlyphProvider$1(this, sprite));
   }
}
