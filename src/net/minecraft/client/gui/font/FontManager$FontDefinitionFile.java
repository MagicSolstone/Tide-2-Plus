package net.minecraft.client.gui.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition$Conditional;

@Environment(EnvType.CLIENT)
record FontManager$FontDefinitionFile(List<GlyphProviderDefinition$Conditional> providers) {
   public static final Codec<FontManager$FontDefinitionFile> CODEC = RecordCodecBuilder.create(
      i -> i.group(GlyphProviderDefinition$Conditional.CODEC.listOf().fieldOf("providers").forGetter(FontManager$FontDefinitionFile::providers))
         .apply(i, FontManager$FontDefinitionFile::new)
   );
}
