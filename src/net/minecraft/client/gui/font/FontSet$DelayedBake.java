package net.minecraft.client.gui.font;

import com.mojang.blaze3d.font.UnbakedGlyph;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class FontSet$DelayedBake implements Supplier<BakedGlyph> {
   private final UnbakedGlyph unbaked;
   @Nullable
   private BakedGlyph baked;

   private FontSet$DelayedBake(final FontSet this$0, final UnbakedGlyph unbaked) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.unbaked = unbaked;
   }

   public BakedGlyph get() {
      if (this.baked == null) {
         this.baked = this.unbaked.bake(this.this$0.wrappedStitcher);
      }

      return this.baked;
   }
}
