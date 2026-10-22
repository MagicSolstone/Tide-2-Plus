package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.CodepointMap;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

@Environment(EnvType.CLIENT)
public record BitmapProvider$Definition(Identifier file, int height, int ascent, int[][] codepointGrid) implements GlyphProviderDefinition {
   private static final Codec<int[][]> CODEPOINT_GRID_CODEC = Codec.STRING.listOf().xmap(input -> {
      int lineCount = input.size();
      int[][] result = new int[lineCount][];

      for (int i = 0; i < lineCount; i++) {
         result[i] = ((String)input.get(i)).codePoints().toArray();
      }

      return result;
   }, grid -> {
      List<String> result = new ArrayList<>(grid.length);

      for (int[] line : grid) {
         result.add(new String(line, 0, line.length));
      }

      return result;
   }).validate(BitmapProvider$Definition::validateDimensions);
   public static final MapCodec<BitmapProvider$Definition> CODEC = RecordCodecBuilder.mapCodec(
         i -> i.group(
               Identifier.CODEC.fieldOf("file").forGetter(BitmapProvider$Definition::file),
               Codec.INT.optionalFieldOf("height", 8).forGetter(BitmapProvider$Definition::height),
               Codec.INT.fieldOf("ascent").forGetter(BitmapProvider$Definition::ascent),
               CODEPOINT_GRID_CODEC.fieldOf("chars").forGetter(BitmapProvider$Definition::codepointGrid)
            )
            .apply(i, BitmapProvider$Definition::new)
      )
      .validate(BitmapProvider$Definition::validate);

   private static DataResult<int[][]> validateDimensions(final int[][] grid) {
      int lineCount = grid.length;
      if (lineCount == 0) {
         return DataResult.error(() -> "Expected to find data in codepoint grid");
      } else {
         int[] firstLine = grid[0];
         int lineWidth = firstLine.length;
         if (lineWidth == 0) {
            return DataResult.error(() -> "Expected to find data in codepoint grid");
         } else {
            for (int i = 1; i < lineCount; i++) {
               int[] line = grid[i];
               if (line.length != lineWidth) {
                  return DataResult.error(
                     () -> "Lines in codepoint grid have to be the same length (found: "
                        + line.length
                        + " codepoints, expected: "
                        + lineWidth
                        + "), pad with \\u0000"
                  );
               }
            }

            return DataResult.success(grid);
         }
      }
   }

   private static DataResult<BitmapProvider$Definition> validate(final BitmapProvider$Definition builder) {
      return builder.ascent > builder.height
         ? DataResult.error(() -> "Ascent " + builder.ascent + " higher than height " + builder.height)
         : DataResult.success(builder);
   }

   @Override
   public GlyphProviderType type() {
      return GlyphProviderType.BITMAP;
   }

   @Override
   public Either<GlyphProviderDefinition$Loader, GlyphProviderDefinition$Reference> unpack() {
      return Either.left(this::load);
   }

   private GlyphProvider load(final ResourceManager resourceManager) throws IOException {
      Identifier texture = this.file.withPrefix("textures/");

      BitmapProvider var23;
      try (InputStream resource = resourceManager.open(texture)) {
         NativeImage image = NativeImage.read(resource);
         int w = image.getWidth();
         int h = image.getHeight();
         int glyphWidth = w / this.codepointGrid[0].length;
         int glyphHeight = h / this.codepointGrid.length;
         float pixelScale = (float)this.height / glyphHeight;
         CodepointMap<BitmapProvider$Glyph> charMap = new CodepointMap<>(BitmapProvider$Glyph[]::new, BitmapProvider$Glyph[][]::new);
         BitmapProvider$ImageDataHolder imageDataHolder = new BitmapProvider$ImageDataHolder(texture, image);

         for (int slotY = 0; slotY < this.codepointGrid.length; slotY++) {
            int linePos = 0;

            for (int c : this.codepointGrid[slotY]) {
               int slotX = linePos++;
               if (c != 0) {
                  int actualGlyphWidth = this.getActualGlyphWidth(image, glyphWidth, glyphHeight, slotX, slotY);
                  BitmapProvider$Glyph prev = charMap.put(
                     c,
                     new BitmapProvider$Glyph(
                        pixelScale,
                        imageDataHolder,
                        slotX * glyphWidth,
                        slotY * glyphHeight,
                        glyphWidth,
                        glyphHeight,
                        (int)(0.5 + actualGlyphWidth * pixelScale) + 1,
                        this.ascent
                     )
                  );
                  if (prev != null) {
                     BitmapProvider.LOGGER.warn("Codepoint '{}' declared multiple times in {}", Integer.toHexString(c), texture);
                  }
               }
            }
         }

         var23 = new BitmapProvider(imageDataHolder, charMap);
      }

      return var23;
   }

   private int getActualGlyphWidth(final NativeImage image, final int glyphWidth, final int glyphHeight, final int xGlyph, final int yGlyph) {
      int width;
      for (width = glyphWidth - 1; width >= 0; width--) {
         int xPixel = xGlyph * glyphWidth + width;

         for (int y = 0; y < glyphHeight; y++) {
            int yPixel = yGlyph * glyphHeight + y;
            if (image.getLuminanceOrAlpha(xPixel, yPixel) != 0) {
               return width + 1;
            }
         }
      }

      return width + 1;
   }
}
