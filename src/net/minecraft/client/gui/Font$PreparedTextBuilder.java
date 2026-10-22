package net.minecraft.client.gui;

import com.mojang.blaze3d.font.GlyphInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.EmptyArea;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.font.TextRenderable$Styled;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class Font$PreparedTextBuilder implements Font$PreparedText, FormattedCharSink {
   private final boolean drawShadow;
   private final int color;
   private final int backgroundColor;
   private final boolean includeEmpty;
   private float x;
   private float y;
   private float left;
   private float top;
   private float right;
   private float bottom;
   private float backgroundLeft;
   private float backgroundTop;
   private float backgroundRight;
   private float backgroundBottom;
   private final List<TextRenderable$Styled> glyphs;
   @Nullable
   private List<TextRenderable> effects;
   @Nullable
   private List<EmptyArea> emptyAreas;

   public Font$PreparedTextBuilder(final Font this$0, final float x, final float y, final int color, final boolean drawShadow, final boolean includeEmpty) {
      this(this$0x, x, y, color, 0, drawShadow, includeEmpty);
   }

   public Font$PreparedTextBuilder(
      final Font this$0, final float x, final float y, final int color, final int backgroundColor, final boolean drawShadow, final boolean includeEmpty
   ) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.left = Float.MAX_VALUE;
      this.top = Float.MAX_VALUE;
      this.right = -Float.MAX_VALUE;
      this.bottom = -Float.MAX_VALUE;
      this.backgroundLeft = Float.MAX_VALUE;
      this.backgroundTop = Float.MAX_VALUE;
      this.backgroundRight = -Float.MAX_VALUE;
      this.backgroundBottom = -Float.MAX_VALUE;
      this.glyphs = new ArrayList<>();
      this.x = x;
      this.y = y;
      this.drawShadow = drawShadow;
      this.color = color;
      this.backgroundColor = backgroundColor;
      this.includeEmpty = includeEmpty;
      this.markBackground(x, y, 0.0F);
   }

   private void markSize(final float left, final float top, final float right, final float bottom) {
      this.left = Math.min(this.left, left);
      this.top = Math.min(this.top, top);
      this.right = Math.max(this.right, right);
      this.bottom = Math.max(this.bottom, bottom);
   }

   private void markBackground(final float x, final float y, final float advance) {
      if (ARGB.alpha(this.backgroundColor) != 0) {
         this.backgroundLeft = Math.min(this.backgroundLeft, x - 1.0F);
         this.backgroundTop = Math.min(this.backgroundTop, y - 1.0F);
         this.backgroundRight = Math.max(this.backgroundRight, x + advance);
         this.backgroundBottom = Math.max(this.backgroundBottom, y + 9.0F);
         this.markSize(this.backgroundLeft, this.backgroundTop, this.backgroundRight, this.backgroundBottom);
      }
   }

   private void addGlyph(final TextRenderable$Styled instance) {
      this.glyphs.add(instance);
      this.markSize(instance.left(), instance.top(), instance.right(), instance.bottom());
   }

   private void addEffect(final TextRenderable effect) {
      if (this.effects == null) {
         this.effects = new ArrayList<>();
      }

      this.effects.add(effect);
      this.markSize(effect.left(), effect.top(), effect.right(), effect.bottom());
   }

   private void addEmptyGlyph(final EmptyArea empty) {
      if (this.emptyAreas == null) {
         this.emptyAreas = new ArrayList<>();
      }

      this.emptyAreas.add(empty);
   }

   @Override
   public boolean accept(final int position, final Style style, final int c) {
      BakedGlyph glyph = this.this$0.getGlyph(c, style);
      return this.accept(position, style, glyph);
   }

   public boolean accept(final int position, final Style style, final BakedGlyph glyph) {
      GlyphInfo glyphInfo = glyph.info();
      boolean bold = style.isBold();
      TextColor styleColor = style.getColor();
      int textColor = this.getTextColor(styleColor);
      int shadowColor = this.getShadowColor(style, textColor);
      float advance = glyphInfo.getAdvance(bold);
      float effectX0 = position == 0 ? this.x - 1.0F : this.x;
      float shadowOffset = glyphInfo.getShadowOffset();
      float boldOffset = bold ? glyphInfo.getBoldOffset() : 0.0F;
      TextRenderable$Styled instance = glyph.createGlyph(this.x, this.y, textColor, shadowColor, style, boldOffset, shadowOffset);
      if (instance != null) {
         this.addGlyph(instance);
      } else if (this.includeEmpty) {
         this.addEmptyGlyph(new EmptyArea(this.x, this.y, advance, 7.0F, 9.0F, style));
      }

      this.markBackground(this.x, this.y, advance);
      if (style.isStrikethrough()) {
         this.addEffect(
            this.this$0
               .provider
               .effect()
               .createEffect(effectX0, this.y + 4.5F - 1.0F, this.x + advance, this.y + 4.5F, 0.01F, textColor, shadowColor, shadowOffset)
         );
      }

      if (style.isUnderlined()) {
         this.addEffect(
            this.this$0
               .provider
               .effect()
               .createEffect(effectX0, this.y + 9.0F - 1.0F, this.x + advance, this.y + 9.0F, 0.01F, textColor, shadowColor, shadowOffset)
         );
      }

      this.x += advance;
      return true;
   }

   @Override
   public void visit(final Font$GlyphVisitor visitor) {
      if (ARGB.alpha(this.backgroundColor) != 0) {
         visitor.acceptEffect(
            this.this$0
               .provider
               .effect()
               .createEffect(this.backgroundLeft, this.backgroundTop, this.backgroundRight, this.backgroundBottom, -0.01F, this.backgroundColor, 0, 0.0F)
         );
      }

      for (TextRenderable$Styled glyph : this.glyphs) {
         visitor.acceptGlyph(glyph);
      }

      if (this.effects != null) {
         for (TextRenderable effect : this.effects) {
            visitor.acceptEffect(effect);
         }
      }

      if (this.emptyAreas != null) {
         for (EmptyArea emptyArea : this.emptyAreas) {
            visitor.acceptEmptyArea(emptyArea);
         }
      }
   }

   public void discardEffects() {
      this.effects = null;
   }

   private int getTextColor(@Nullable final TextColor textColor) {
      if (textColor != null) {
         int alpha = ARGB.alpha(this.color);
         int rgb = textColor.getValue();
         return ARGB.color(alpha, rgb);
      } else {
         return this.color;
      }
   }

   private int getShadowColor(final Style style, final int textColor) {
      Integer shadow = style.getShadowColor();
      if (shadow != null) {
         float textAlpha = ARGB.alphaFloat(textColor);
         float shadowAlpha = ARGB.alphaFloat(shadow);
         return textAlpha != 1.0F ? ARGB.color(ARGB.as8BitChannel(textAlpha * shadowAlpha), shadow) : shadow;
      } else {
         return this.drawShadow ? ARGB.scaleRGB(textColor, 0.25F) : 0;
      }
   }

   @Nullable
   @Override
   public ScreenRectangle bounds() {
      if (!(this.left >= this.right) && !(this.top >= this.bottom)) {
         int left = Mth.floor(this.left);
         int top = Mth.floor(this.top);
         int right = Mth.ceil(this.right);
         int bottom = Mth.ceil(this.bottom);
         return new ScreenRectangle(left, top, right - left, bottom - top);
      } else {
         return null;
      }
   }
}
