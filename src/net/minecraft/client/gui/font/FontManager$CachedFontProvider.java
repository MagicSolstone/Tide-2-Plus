package net.minecraft.client.gui.font;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font$Provider;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.glyphs.EffectGlyph;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.FontDescription$AtlasSprite;
import net.minecraft.network.chat.FontDescription$PlayerSprite;
import net.minecraft.network.chat.FontDescription$Resource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class FontManager$CachedFontProvider implements Font$Provider, AutoCloseable {
   private final boolean nonFishyOnly;
   @Nullable
   private volatile FontManager$CachedFontProvider$CachedEntry lastEntry;
   @Nullable
   private volatile EffectGlyph whiteGlyph;

   private FontManager$CachedFontProvider(final FontManager this$0, final boolean nonFishyOnly) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.nonFishyOnly = nonFishyOnly;
   }

   public void invalidate() {
      this.lastEntry = null;
      this.whiteGlyph = null;
   }

   @Override
   public void close() {
      this.invalidate();
   }

   private GlyphSource getGlyphSource(final FontDescription description) {
      return switch (description) {
         case FontDescription$Resource resource -> this.this$0.getFontSetRaw(resource.id()).source(this.nonFishyOnly);
         case FontDescription$AtlasSprite sprite -> this.this$0.getSpriteFont(sprite);
         case FontDescription$PlayerSprite player -> this.this$0.playerProvider.sourceForPlayer(player);
         default -> this.this$0.missingFontSet.source(this.nonFishyOnly);
      };
   }

   @Override
   public GlyphSource glyphs(final FontDescription description) {
      FontManager$CachedFontProvider$CachedEntry lastEntry = this.lastEntry;
      if (lastEntry != null && description.equals(lastEntry.description)) {
         return lastEntry.source;
      } else {
         GlyphSource result = this.getGlyphSource(description);
         this.lastEntry = new FontManager$CachedFontProvider$CachedEntry(description, result);
         return result;
      }
   }

   @Override
   public EffectGlyph effect() {
      EffectGlyph whiteGlyph = this.whiteGlyph;
      if (whiteGlyph == null) {
         whiteGlyph = this.this$0.getFontSetRaw(FontDescription.DEFAULT.id()).whiteGlyph();
         this.whiteGlyph = whiteGlyph;
      }

      return whiteGlyph;
   }
}
