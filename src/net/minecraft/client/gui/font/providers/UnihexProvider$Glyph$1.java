package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphInfo;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class UnihexProvider$Glyph$1 implements GlyphInfo {
   UnihexProvider$Glyph$1(final UnihexProvider$Glyph this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public float getAdvance() {
      return this.this$0.width() / 2 + 1;
   }

   @Override
   public float getShadowOffset() {
      return 0.5F;
   }

   @Override
   public float getBoldOffset() {
      return 0.5F;
   }
}
