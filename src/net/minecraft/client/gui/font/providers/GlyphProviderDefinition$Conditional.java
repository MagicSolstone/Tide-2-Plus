package net.minecraft.client.gui.font.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.FontOption$Filter;

@Environment(EnvType.CLIENT)
public record GlyphProviderDefinition$Conditional(GlyphProviderDefinition definition, FontOption$Filter filter) {
   public static final Codec<GlyphProviderDefinition$Conditional> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            GlyphProviderDefinition.MAP_CODEC.forGetter(GlyphProviderDefinition$Conditional::definition),
            FontOption$Filter.CODEC.optionalFieldOf("filter", FontOption$Filter.ALWAYS_PASS).forGetter(GlyphProviderDefinition$Conditional::filter)
         )
         .apply(i, GlyphProviderDefinition$Conditional::new)
   );
}
