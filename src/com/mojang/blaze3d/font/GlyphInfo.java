package com.mojang.blaze3d.font;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface GlyphInfo {
   float getAdvance();

   default float getAdvance(final boolean bold) {
      return this.getAdvance() + (bold ? this.getBoldOffset() : 0.0F);
   }

   default float getBoldOffset() {
      return 1.0F;
   }

   default float getShadowOffset() {
      return 1.0F;
   }

   static GlyphInfo simple(final float advance) {
      return () -> advance;
   }
}
