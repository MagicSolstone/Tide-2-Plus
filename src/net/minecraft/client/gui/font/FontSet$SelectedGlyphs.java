package net.minecraft.client.gui.font;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;

@Environment(EnvType.CLIENT)
record FontSet$SelectedGlyphs(Supplier<BakedGlyph> any, Supplier<BakedGlyph> nonFishy) {
   private Supplier<BakedGlyph> select(final boolean filterFishy) {
      return filterFishy ? this.nonFishy : this.any;
   }
}
