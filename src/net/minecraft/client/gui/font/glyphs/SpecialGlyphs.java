package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.NativeImage$Format;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.GlyphStitcher;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public enum SpecialGlyphs implements GlyphInfo {
   WHITE(() -> generate(5, 8, (x, y) -> -1)),
   MISSING(() -> {
      int width = 5;
      int height = 8;
      return generate(5, 8, (x, y) -> {
         boolean edge = x == 0 || x + 1 == 5 || y == 0 || y + 1 == 8;
         return edge ? -1 : 0;
      });
   });

   private final NativeImage image;

   private static NativeImage generate(final int width, final int height, final SpecialGlyphs$PixelProvider pixelProvider) {
      NativeImage result = new NativeImage(NativeImage$Format.RGBA, width, height, false);

      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            result.setPixel(x, y, pixelProvider.getColor(x, y));
         }
      }

      result.untrack();
      return result;
   }

   private SpecialGlyphs(final Supplier<NativeImage> image) {
      this.image = image.get();
   }

   @Override
   public float getAdvance() {
      return this.image.getWidth() + 1;
   }

   @Nullable
   public BakedSheetGlyph bake(final GlyphStitcher stitcher) {
      return stitcher.stitch(this, new SpecialGlyphs$1(this));
   }
}
