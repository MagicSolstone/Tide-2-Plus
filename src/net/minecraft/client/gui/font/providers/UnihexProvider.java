package net.minecraft.client.gui.font.providers;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.UnbakedGlyph;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.CodepointMap;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class UnihexProvider implements GlyphProvider {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int GLYPH_HEIGHT = 16;
   private static final int DIGITS_PER_BYTE = 2;
   private static final int DIGITS_FOR_WIDTH_8 = 32;
   private static final int DIGITS_FOR_WIDTH_16 = 64;
   private static final int DIGITS_FOR_WIDTH_24 = 96;
   private static final int DIGITS_FOR_WIDTH_32 = 128;
   private final CodepointMap<UnihexProvider$Glyph> glyphs;

   private UnihexProvider(final CodepointMap<UnihexProvider$Glyph> glyphs) {
      this.glyphs = glyphs;
   }

   @Nullable
   @Override
   public UnbakedGlyph getGlyph(final int codepoint) {
      return this.glyphs.get(codepoint);
   }

   @Override
   public IntSet getSupportedGlyphs() {
      return this.glyphs.keySet();
   }

   @VisibleForTesting
   static void unpackBitsToBytes(final IntBuffer output, final int value, final int left, final int right) {
      int startBit = 32 - left - 1;
      int endBit = 32 - right - 1;

      for (int i = startBit; i >= endBit; i--) {
         if (i < 32 && i >= 0) {
            boolean isSet = (value >> i & 1) != 0;
            output.put(isSet ? -1 : 0);
         } else {
            output.put(0);
         }
      }
   }

   private static void unpackBitsToBytes(final IntBuffer output, final UnihexProvider$LineData data, final int left, final int right) {
      for (int i = 0; i < 16; i++) {
         int line = data.line(i);
         unpackBitsToBytes(output, line, left, right);
      }
   }

   @VisibleForTesting
   static void readFromStream(final InputStream input, final UnihexProvider$ReaderOutput output) throws IOException {
      int line = 0;
      ByteList buffer = new ByteArrayList(128);

      while (true) {
         boolean foundColon = copyUntil(input, buffer, 58);
         int codepointDigitCount = buffer.size();
         if (codepointDigitCount == 0 && !foundColon) {
            return;
         }

         if (!foundColon || codepointDigitCount != 4 && codepointDigitCount != 5 && codepointDigitCount != 6) {
            throw new IllegalArgumentException("Invalid entry at line " + line + ": expected 4, 5 or 6 hex digits followed by a colon");
         }

         int codepoint = 0;

         for (int i = 0; i < codepointDigitCount; i++) {
            codepoint = codepoint << 4 | decodeHex(line, buffer.getByte(i));
         }

         buffer.clear();
         copyUntil(input, buffer, 10);
         int dataDigitCount = buffer.size();

         UnihexProvider$LineData contents = switch (dataDigitCount) {
            case 32 -> UnihexProvider$ByteContents.read(line, buffer);
            case 64 -> UnihexProvider$ShortContents.read(line, buffer);
            case 96 -> UnihexProvider$IntContents.read24(line, buffer);
            case 128 -> UnihexProvider$IntContents.read32(line, buffer);
            default -> throw new IllegalArgumentException(
               "Invalid entry at line " + line + ": expected hex number describing (8,16,24,32) x 16 bitmap, followed by a new line"
            );
         };
         output.accept(codepoint, contents);
         line++;
         buffer.clear();
      }
   }

   private static int decodeHex(final int line, final ByteList input, final int index) {
      return decodeHex(line, input.getByte(index));
   }

   private static int decodeHex(final int line, final byte b) {
      return switch (b) {
         case 48 -> 0;
         case 49 -> 1;
         case 50 -> 2;
         case 51 -> 3;
         case 52 -> 4;
         case 53 -> 5;
         case 54 -> 6;
         case 55 -> 7;
         case 56 -> 8;
         case 57 -> 9;
         default -> throw new IllegalArgumentException("Invalid entry at line " + line + ": expected hex digit, got " + (char)b);
         case 65 -> 10;
         case 66 -> 11;
         case 67 -> 12;
         case 68 -> 13;
         case 69 -> 14;
         case 70 -> 15;
      };
   }

   private static boolean copyUntil(final InputStream input, final ByteList output, final int delimiter) throws IOException {
      while (true) {
         int b = input.read();
         if (b == -1) {
            return false;
         }

         if (b == delimiter) {
            return true;
         }

         output.add((byte)b);
      }
   }
}
