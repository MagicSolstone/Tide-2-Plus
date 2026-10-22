package net.minecraft.client.resources.palette;

import com.google.common.cache.CacheLoader;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class PaletteMappingCache$1 extends CacheLoader<PaletteMappingCache$IdPair, PaletteMapping> {
   PaletteMappingCache$1(final PaletteMappingCache this$0, final Map val$palettes) {
      this.val$palettes = val$palettesx;
      Objects.requireNonNull(this$0);
      super();
   }

   public PaletteMapping load(final PaletteMappingCache$IdPair pair) {
      Palette basePalette = (Palette)this.val$palettes.get(pair.base);
      Palette targetPalette = (Palette)this.val$palettes.get(pair.target);
      if (basePalette == null) {
         PaletteMappingCache.LOGGER.warn("Couldn't find base palette {}", pair.base);
         return PaletteMapping.NONE;
      } else if (targetPalette == null) {
         PaletteMappingCache.LOGGER.warn("Couldn't find target palette {}", pair.target);
         return PaletteMapping.NONE;
      } else if (basePalette.size() != targetPalette.size()) {
         PaletteMappingCache.LOGGER.warn("Could not create palette mapping for {}, had different sizes", pair);
         return PaletteMapping.NONE;
      } else {
         return PaletteMapping.create(basePalette, targetPalette);
      }
   }
}
