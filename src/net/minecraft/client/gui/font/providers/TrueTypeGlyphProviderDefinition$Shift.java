package net.minecraft.client.gui.font.providers;

import com.mojang.serialization.Codec;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public record TrueTypeGlyphProviderDefinition$Shift(float x, float y) {
   public static final TrueTypeGlyphProviderDefinition$Shift NONE = new TrueTypeGlyphProviderDefinition$Shift(0.0F, 0.0F);
   public static final Codec<TrueTypeGlyphProviderDefinition$Shift> CODEC = Codec.floatRange(-512.0F, 512.0F)
      .listOf()
      .comapFlatMap(
         input -> Util.fixedSize(input, 2).map(floats -> new TrueTypeGlyphProviderDefinition$Shift((Float)floats.get(0), (Float)floats.get(1))),
         shift -> List.of(shift.x, shift.y)
      );
}
