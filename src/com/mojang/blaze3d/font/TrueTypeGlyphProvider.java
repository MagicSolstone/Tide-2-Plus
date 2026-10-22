package com.mojang.blaze3d.font;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.CodepointMap;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.gui.font.providers.FreeTypeUtil;
import org.jspecify.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.freetype.FT_Bitmap;
import org.lwjgl.util.freetype.FT_Face;
import org.lwjgl.util.freetype.FT_GlyphSlot;
import org.lwjgl.util.freetype.FT_Vector;
import org.lwjgl.util.freetype.FreeType;

@Environment(EnvType.CLIENT)
public class TrueTypeGlyphProvider implements GlyphProvider {
   @Nullable
   private ByteBuffer fontMemory;
   @Nullable
   private FT_Face face;
   private final float oversample;
   private final CodepointMap<TrueTypeGlyphProvider$GlyphEntry> glyphs = new CodepointMap<>(
      TrueTypeGlyphProvider$GlyphEntry[]::new, TrueTypeGlyphProvider$GlyphEntry[][]::new
   );

   public TrueTypeGlyphProvider(
      final ByteBuffer fontMemory, final FT_Face face, final float size, final float oversample, final float shiftX, final float shiftY, final String skip
   ) {
      this.fontMemory = fontMemory;
      this.face = face;
      this.oversample = oversample;
      IntSet skipSet = new IntArraySet();
      skip.codePoints().forEach(skipSet::add);
      int pixelsPerEm = Math.round(size * oversample);
      FreeType.FT_Set_Pixel_Sizes(face, pixelsPerEm, pixelsPerEm);
      float transformX = shiftX * oversample;
      float transformY = -shiftY * oversample;
      MemoryStack stack = MemoryStack.stackPush();

      try {
         FT_Vector vector = FreeTypeUtil.setVector(FT_Vector.malloc(stack), transformX, transformY);
         FreeType.FT_Set_Transform(face, null, vector);
         IntBuffer indexPtr = stack.mallocInt(1);
         int codepoint = (int)FreeType.FT_Get_First_Char(face, indexPtr);

         while (true) {
            int index = indexPtr.get(0);
            if (index == 0) {
               break;
            }

            if (!skipSet.contains(codepoint)) {
               this.glyphs.put(codepoint, new TrueTypeGlyphProvider$GlyphEntry(index));
            }

            codepoint = (int)FreeType.FT_Get_Next_Char(face, codepoint, indexPtr);
         }
      } catch (Throwable var18) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var17) {
               var18.addSuppressed(var17);
            }
         }

         throw var18;
      }

      if (stack != null) {
         stack.close();
      }
   }

   @Nullable
   @Override
   public UnbakedGlyph getGlyph(final int codepoint) {
      TrueTypeGlyphProvider$GlyphEntry entry = this.glyphs.get(codepoint);
      return entry != null ? this.getOrLoadGlyphInfo(codepoint, entry) : null;
   }

   private UnbakedGlyph getOrLoadGlyphInfo(final int codepoint, final TrueTypeGlyphProvider$GlyphEntry entry) {
      UnbakedGlyph result = entry.glyph;
      if (result == null) {
         FT_Face face = this.validateFontOpen();
         synchronized (face) {
            result = entry.glyph;
            if (result == null) {
               result = this.loadGlyph(codepoint, face, entry.index);
               entry.glyph = result;
            }
         }
      }

      return result;
   }

   private UnbakedGlyph loadGlyph(final int codepoint, final FT_Face face, final int index) {
      int errorCode = FreeType.FT_Load_Glyph(face, index, 4194312);
      if (errorCode != 0) {
         FreeTypeUtil.assertError(errorCode, String.format(Locale.ROOT, "Loading glyph U+%06X", codepoint));
      }

      FT_GlyphSlot glyph = face.glyph();
      if (glyph == null) {
         throw new NullPointerException(String.format(Locale.ROOT, "Glyph U+%06X not initialized", codepoint));
      } else {
         float scaledAdvance = FreeTypeUtil.x(glyph.advance());
         FT_Bitmap bitmap = glyph.bitmap();
         int left = glyph.bitmap_left();
         int top = glyph.bitmap_top();
         int width = bitmap.width();
         int height = bitmap.rows();
         return (UnbakedGlyph)(width > 0 && height > 0
            ? new TrueTypeGlyphProvider$Glyph(this, (float)left, (float)top, width, height, scaledAdvance, index)
            : new EmptyGlyph(scaledAdvance / this.oversample));
      }
   }

   private FT_Face validateFontOpen() {
      if (this.fontMemory != null && this.face != null) {
         return this.face;
      } else {
         throw new IllegalStateException("Provider already closed");
      }
   }

   @Override
   public void close() {
      if (this.face != null) {
         synchronized (FreeTypeUtil.LIBRARY_LOCK) {
            FreeTypeUtil.checkError(FreeType.FT_Done_Face(this.face), "Deleting face");
         }

         this.face = null;
      }

      MemoryUtil.memFree(this.fontMemory);
      this.fontMemory = null;
   }

   @Override
   public IntSet getSupportedGlyphs() {
      return this.glyphs.keySet();
   }
}
